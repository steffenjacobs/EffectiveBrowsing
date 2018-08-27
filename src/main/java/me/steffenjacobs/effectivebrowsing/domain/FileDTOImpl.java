package me.steffenjacobs.effectivebrowsing.domain;

/** @author Steffen Jacobs */
public class FileDTOImpl implements FileDTO {

	String name, type, absolutePath;
	
	public FileDTOImpl(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

}
