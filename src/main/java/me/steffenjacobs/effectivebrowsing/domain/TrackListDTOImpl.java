package me.steffenjacobs.effectivebrowsing.domain;

import java.util.List;

/** @author Steffen Jacobs */
public class TrackListDTOImpl {
	List<TrackDTO> tracks;

	public TrackListDTOImpl(List<TrackDTO> tracks) {
		this.tracks = tracks;
	}

	public TrackListDTOImpl() {

	}

	public List<TrackDTO> getTracks() {
		return tracks;
	}

	public void setTracks(List<TrackDTO> tracks) {
		this.tracks = tracks;
	}
}
