package me.steffenjacobs.effectivebrowsing.domain;

import java.util.Collection;

/** @author Steffen Jacobs */
public interface BrowseResult {
	
	public Collection<FileDTO> getFiles();
	
	public void setFiles(Collection<FileDTO> files);
	
	public String getDirectoryPath();
	
	public void setDirectoryPath(String directoryPath);

}
