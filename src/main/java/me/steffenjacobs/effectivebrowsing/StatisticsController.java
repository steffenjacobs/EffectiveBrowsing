package me.steffenjacobs.effectivebrowsing;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import me.steffenjacobs.effectivebrowsing.domain.orm.TrackInfo;
import me.steffenjacobs.effectivebrowsing.domain.orm.TrackRepository;

/** @author Steffen Jacobs */

@Controller
public class StatisticsController {

	@Autowired
	TrackRepository trackRepository;
	
	@Autowired
	StatisticsService statisticsService;


	@PostMapping(value = "/files/statistics/listencount", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<String> onTrackListenedTo(String title, String artist, long length) throws FileNotFoundException {

		List<TrackInfo> info = trackRepository.findByTitleAndArtistAndLength(title, artist, length);
		if (!info.isEmpty()) {
			statisticsService.executeUpdateQuery(info.get(0).getId());
			return new ResponseEntity<>(HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}
}
