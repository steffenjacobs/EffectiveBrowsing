package me.steffenjacobs.effectivebrowsing.domain.orm;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/** @author Steffen Jacobs */

@Entity(name = "track")
@Table(name = "track")
public class TrackInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column
	private String artist;
	@Column
	private String album;
	@Column
	private String title;
	@Column
	private String comment;
	@Column
	private String year;
	@Column
	private String track;
	@Column
	private String discNo;
	@Column
	private String composer;
	@Column
	private String artistSort;
	@Column
	private String genre;

	@Column
	private String path;

	@Column
	private long length;

	public TrackInfo(Tag tag, long trackLength, String path) {
		artist = tag.getFirst(FieldKey.ARTIST);
		album = tag.getFirst(FieldKey.ALBUM);
		title = tag.getFirst(FieldKey.TITLE);
		comment = tag.getFirst(FieldKey.COMMENT);
		year = tag.getFirst(FieldKey.YEAR);
		track = tag.getFirst(FieldKey.TRACK);
		discNo = tag.getFirst(FieldKey.DISC_NO);
		composer = tag.getFirst(FieldKey.COMPOSER);
		artistSort = tag.getFirst(FieldKey.ARTIST_SORT);
		length = trackLength;
		this.path = path;
	}

	public TrackInfo() {
	}

	public boolean isEmpty() {
		return path == null || "".equals(path);
	}

	public String getDiscNo() {
		return discNo;
	}

	public void setDiscNo(String discNo) {
		this.discNo = discNo;
	}

	public String getArtistSort() {
		return artistSort;
	}

	public void setArtistSort(String artistSort) {
		this.artistSort = artistSort;
	}

	public long getId() {
		return id;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(String artist) {
		this.artist = artist;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getTrack() {
		return track;
	}

	public void setTrack(String track) {
		this.track = track;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((artist == null) ? 0 : artist.hashCode());
		result = prime * result + (int) (length ^ (length >>> 32));
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TrackInfo other = (TrackInfo) obj;
		if (artist == null) {
			if (other.artist != null)
				return false;
		} else if (!artist.equals(other.artist))
			return false;
		if (length != other.length)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

}
