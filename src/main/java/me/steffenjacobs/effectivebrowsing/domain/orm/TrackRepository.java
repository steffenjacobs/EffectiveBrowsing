package me.steffenjacobs.effectivebrowsing.domain.orm;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** @author Steffen Jacobs */
@Repository("trackRepository")
public interface TrackRepository extends JpaRepository<TrackInfo, Long> {

	public List<TrackInfo> findByTitleContainingIgnoreCase(String title);

	public List<TrackInfo> findByArtistContainingIgnoreCase(String artist);

	public List<TrackInfo> findByTitleAndArtistAndLength(String title, String artist, long length);

	public List<TrackInfo> findByArtistContainingIgnoreCaseOrTitleContainingIgnoreCaseOrAlbumContainingIgnoreCaseOrComposerContainingIgnoreCaseOrCommentContainingIgnoreCaseOrArtistSortContainingIgnoreCaseOrGenreContainingIgnoreCase(
			String artist, String title, String album, String composer, String comment, String artistSort, String genre);

	public List<TrackInfo> findByYearBetween(Date year1, Date year2);
}
