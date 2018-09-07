package me.steffenjacobs.effectivebrowsing;

import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import me.steffenjacobs.effectivebrowsing.domain.orm.TrackInfo;
import me.steffenjacobs.effectivebrowsing.domain.orm.TrackRepository;

/** @author Steffen Jacobs */
@Controller
public class SearchController {

	@Autowired
	TrackRepository trackRepository;

	private static final Calendar CAL = Calendar.getInstance();

	static {
		CAL.setTimeInMillis(0);
	}

	@GetMapping(value = "/files/search/title")
	public ResponseEntity<Collection<TrackInfo>> searchByTitle(String title) throws FileNotFoundException {
		return new ResponseEntity<>(trackRepository.findByTitleContainingIgnoreCase(title), HttpStatus.OK);
	}

	@GetMapping(value = "/files/search/artist")
	public ResponseEntity<Collection<TrackInfo>> searchByArtist(String artist) throws FileNotFoundException {
		return new ResponseEntity<>(trackRepository.findByArtistContainingIgnoreCase(artist), HttpStatus.OK);
	}

	@GetMapping(value = "/files/search/year")
	public ResponseEntity<Collection<TrackInfo>> searchByYear(int year1, int year2) throws FileNotFoundException {
		CAL.set(Calendar.YEAR, year1);
		Date date1 = CAL.getTime();
		CAL.set(Calendar.YEAR, year2);
		Date date2 = CAL.getTime();
		return new ResponseEntity<>(trackRepository.findByYearBetween(date1, date2), HttpStatus.OK);
	}

	@GetMapping(value = "/files/search")
	public ResponseEntity<Collection<TrackInfo>> search(String search) throws FileNotFoundException {
		return new ResponseEntity<>(trackRepository
				.findByArtistContainingIgnoreCaseOrTitleContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrComposerContainingIgnoreCaseOrCommentContainingIgnoreCaseOrArtistSortContainingIgnoreCaseOrGenreContainingIgnoreCase(
						search, search, search, search, search, search, search),
				HttpStatus.OK);
	}

	@GetMapping(value = "files/search/duplicate")
	public ResponseEntity<Collection<TrackInfo>> searchDuplicates() throws FileNotFoundException {
		String title = "Night of the Hunter", artist = "30 Seconds to Mars";
		long length = 340000;
		return new ResponseEntity<>(trackRepository.findByTitleAndArtistAndLength(title, artist, length), HttpStatus.OK);
	}
}
