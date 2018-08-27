package me.steffenjacobs.effectivebrowsing.domain;

/** @author Steffen Jacobs */
public interface FileDTO {

	public String getAbsolutePath();

	public void setAbsolutePath(String absolutePath);

	public String getName();

	public void setName(String name);

	public String getType();

	public void setType(String type);

}
