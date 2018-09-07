package me.steffenjacobs.effectivebrowsing.domain;

/** @author Steffen Jacobs */
public class IndexingStatusDTO {

	public enum IndexingStatus {
		STARTING, INDEXING, FINISHED;
	}

	private long filesToIndex, filesIndexed, numDirectories;
	private IndexingStatus indexingStatus;

	public IndexingStatusDTO() {
		super();
	}

	public IndexingStatusDTO(IndexingStatus indexingStatus) {
		super();
		this.indexingStatus = indexingStatus;
	}

	public IndexingStatusDTO(long filesToIndex, long filesIndexed, long numDirectories, IndexingStatus indexingStatus) {
		super();
		this.filesToIndex = filesToIndex;
		this.filesIndexed = filesIndexed;
		this.numDirectories = numDirectories;
		this.indexingStatus = indexingStatus;
	}

	public long getFilesToIndex() {
		return filesToIndex;
	}

	public void setFilesToIndex(long filesToIndex) {
		this.filesToIndex = filesToIndex;
	}

	public long getFilesIndexed() {
		return filesIndexed;
	}

	public void setFilesIndexed(long filesIndex) {
		this.filesIndexed = filesIndex;
	}

	public long getNumDirectories() {
		return numDirectories;
	}

	public void setNumDirectories(long numDirectories) {
		this.numDirectories = numDirectories;
	}

	public IndexingStatus getIndexingStatus() {
		return indexingStatus;
	}

	public void setIndexingStatus(IndexingStatus indexingStatus) {
		this.indexingStatus = indexingStatus;
	}

}
