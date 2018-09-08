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

import me.steffenjacobs.effectivebrowsing.domain.TrackDTO;
import me.steffenjacobs.effectivebrowsing.domain.TrackDTOFactory;
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
	public ResponseEntity<Collection<TrackDTO>> searchByTitle(String title) throws FileNotFoundException {
		return new ResponseEntity<>(TrackDTOFactory.fromTrackInfo(trackRepository.findByTitleContainingIgnoreCase(title)), HttpStatus.OK);
	}

	@GetMapping(value = "/files/search/artist")
	public ResponseEntity<Collection<TrackDTO>> searchByArtist(String artist) throws FileNotFoundException {
		return new ResponseEntity<>(TrackDTOFactory.fromTrackInfo(trackRepository.findByArtistContainingIgnoreCase(artist)), HttpStatus.OK);
	}

	@GetMapping(value = "/files/search/year")
	public ResponseEntity<Collection<TrackDTO>> searchByYear(int year1, int year2) throws FileNotFoundException {
		CAL.set(Calendar.YEAR, year1);
		Date date1 = CAL.getTime();
		CAL.set(Calendar.YEAR, year2);
		Date date2 = CAL.getTime();
		return new ResponseEntity<>(TrackDTOFactory.fromTrackInfo(trackRepository.findByYearBetween(date1, date2)), HttpStatus.OK);
	}

	@GetMapping(value = "/files/search")
	public ResponseEntity<Collection<TrackDTO>> search(String search) throws FileNotFoundException {
		return new ResponseEntity<>(TrackDTOFactory.fromTrackInfo(trackRepository
				.findByArtistContainingIgnoreCaseOrTitleContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrComposerContainingIgnoreCaseOrCommentContainingIgnoreCaseOrArtistSortContainingIgnoreCaseOrGenreContainingIgnoreCase(
						search, search, search, search, search, search, search)),
				HttpStatus.OK);
	}

	@GetMapping(value = "files/search/duplicate")
	public ResponseEntity<Collection<TrackDTO>> searchDuplicates() throws FileNotFoundException {
		String title = "Night of the Hunter", artist = "30 Seconds to Mars";
		long length = 340000;
		return new ResponseEntity<>(TrackDTOFactory.fromTrackInfo(trackRepository.findByTitleAndArtistAndLength(title, artist, length)), HttpStatus.OK);
	}
}
