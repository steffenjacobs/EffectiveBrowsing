package me.steffenjacobs.effectivebrowsing;

import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import me.steffenjacobs.effectivebrowsing.domain.IndexingStatusDTO;

/** @author Steffen Jacobs */

@Controller
public class IndexingController {
	
	private static final Logger LOG = LoggerFactory.getLogger(IndexingController.class);

	@Autowired
	IndexingService indexingService;

	@PostMapping(value = "/files/index", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<IndexingStatusDTO> index(String path) throws Exception {
		LOG.info("Started indexing of " + path);
		return new ResponseEntity<>(indexingService.startIndexing(path), HttpStatus.OK);
	}

	@GetMapping(value = "/files/index/status")
	public ResponseEntity<IndexingStatusDTO> getIndexingStatus() throws FileNotFoundException {
		return new ResponseEntity<>(indexingService.getStatus(), HttpStatus.OK);
	}
}
