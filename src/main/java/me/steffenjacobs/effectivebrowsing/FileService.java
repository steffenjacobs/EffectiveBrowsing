package me.steffenjacobs.effectivebrowsing;

import java.io.File;
import java.io.FileNotFoundException;

import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivebrowsing.domain.BrowseResult;
import me.steffenjacobs.effectivebrowsing.domain.BrowseResultImpl;
import me.steffenjacobs.effectivebrowsing.domain.FileDTO;
import me.steffenjacobs.effectivebrowsing.domain.FileDTOImpl;

/** @author Steffen Jacobs */
@Component
public class FileService {

	public BrowseResult browse(String path) throws FileNotFoundException {
		File f = new File(path);
		if (!f.exists()) {
			throw new FileNotFoundException(path);
		}
		File[] files = f.listFiles();

		BrowseResult result = new BrowseResultImpl();
		result.setDirectoryPath(path);
		for (File file : files) {
			FileDTO dto = new FileDTOImpl();
			dto.setAbsolutePath(file.getAbsolutePath());
			dto.setName(file.getName());
			dto.setType(file.isFile() ? "file" : "directory");
			result.getFiles().add(dto);
		}
		return result;
	}

}
