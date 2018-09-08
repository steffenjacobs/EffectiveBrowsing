package me.steffenjacobs.effectivebrowsing.domain;

import java.util.LinkedList;
import java.util.List;

import me.steffenjacobs.effectivebrowsing.domain.orm.TrackInfo;

/** @author Steffen Jacobs */
public class TrackDTOFactory {

	public static TrackDTO fromTrackInfo(TrackInfo t) {
		TrackDTO dto = new TrackDTO();
		dto.setArtist(t.getArtist());
		dto.setAlbum(t.getAlbum());
		dto.setTitle(t.getTitle());
		dto.setComment(t.getComment());
		dto.setYear(t.getYear());
		dto.setTrack(t.getTrack());
		dto.setDiscNo(t.getDiscNo());
		dto.setComposer(t.getComposer());
		dto.setArtistSort(t.getArtistSort());
		dto.setGenre(t.getGenre());
		dto.setPath(t.getPath());
		dto.setBpm(t.getBpm());
		dto.setBitrate(t.getBitrate());
		dto.setCreationDate(t.getCreationDate());
		dto.setLength(t.getLength());
		return dto;
	}
	
	public static TrackListDTOImpl fromTrackInfo(List<TrackInfo> list){
		List<TrackDTO> info = new LinkedList<>();
		list.forEach(t -> info.add(fromTrackInfo(t)));
		return new TrackListDTOImpl(info);
	}
}
