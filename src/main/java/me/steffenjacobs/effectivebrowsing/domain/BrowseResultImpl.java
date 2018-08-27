package me.steffenjacobs.effectivebrowsing.domain;

import java.util.ArrayList;
import java.util.Collection;

/** @author Steffen Jacobs */
public class BrowseResultImpl implements BrowseResult {

	private Collection<FileDTO> files = new ArrayList<>();

	private String directoryPath;

	public BrowseResultImpl() {

	}

	public Collection<FileDTO> getFiles() {
		return files;
	}

	public void setFiles(Collection<FileDTO> files) {
		this.files = files;
	}

	public String getDirectoryPath() {
		return directoryPath;
	}

	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

}
