package me.steffenjacobs.effectivebrowsing;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import me.steffenjacobs.effectivebrowsing.domain.BrowseResult;

/** @author Steffen Jacobs */
@Controller
public class FileController {
	
	@Autowired
	FileService fileService;


	@PostMapping(value = "/files/browse", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public ResponseEntity<BrowseResult> getBrowse(String path) throws FileNotFoundException {
		return new ResponseEntity<>(fileService.browse(path), HttpStatus.OK);
	}
}
