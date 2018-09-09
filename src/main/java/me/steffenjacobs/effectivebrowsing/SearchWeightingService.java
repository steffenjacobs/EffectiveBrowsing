package me.steffenjacobs.effectivebrowsing;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivebrowsing.domain.TrackDTO;
import me.steffenjacobs.effectivebrowsing.domain.TrackListDTOImpl;

/** @author Steffen Jacobs */
@Component
public class SearchWeightingService {

	public static class WeightedSearchResultList {
		private List<WeightedSearchResult> tracks;

		public List<WeightedSearchResult> getTracks() {
			return tracks;
		}

		public void setTracks(List<WeightedSearchResult> tracks) {
			this.tracks = tracks;
		}

		public WeightedSearchResultList() {
		}

		public WeightedSearchResultList(List<WeightedSearchResult> tracks) {
			super();
			this.tracks = tracks;
		}

	}

	public static class WeightedSearchResult {
		private long weight;
		private TrackDTO track;

		public WeightedSearchResult(long weight, TrackDTO track) {
			super();
			this.weight = weight;
			this.track = track;
		}

		public WeightedSearchResult() {
		}

		public long getWeight() {
			return weight;
		}

		public void setWeight(long weight) {
			this.weight = weight;
		}

		public TrackDTO getTrack() {
			return track;
		}

		public void setTrack(TrackDTO track) {
			this.track = track;
		}
	}

	private WeightedSearchResult weight(TrackDTO track, String[] searchTerms) {
		long weight = 0;

		weight += 2 * weight(track::getTitle, searchTerms);
		weight += 2 * weight(track::getArtist, searchTerms);
		weight += 2 * weight(track::getAlbum, searchTerms);
		weight += weight(track::getComment, searchTerms);
		weight += weight(track::getArtistSort, searchTerms);
		weight += weight(track::getComposer, searchTerms);
		weight += weight(track::getGenre, searchTerms);

		if (weight > 0) {
			weight += (int) (0.5 * track.getListencount());
		}

		return new WeightedSearchResult(weight, track);
	}

	private int weight(Supplier<String> supplier, String[] searchTerms) {
		int weight = 0;
		for (String s : searchTerms) {
			if (s != null && supplier.get() != null && (s.equals(supplier.get()) || s.contains(supplier.get()))) {
				weight++;
			}
		}
		return weight;
	}

	public List<WeightedSearchResult> weightSearchResult(TrackListDTOImpl tracks, String searchString) {
		String[] searchTerms = searchString.split(" ");
		List<WeightedSearchResult> results = tracks.getTracks().parallelStream().map(t -> weight(t, searchTerms)).collect(Collectors.toList());

		results.sort((o1, o2) -> (int) (o2.getWeight() - o1.getWeight()));

		return results;
	}

}
