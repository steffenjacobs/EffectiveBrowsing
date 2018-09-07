package me.steffenjacobs.effectivebrowsing.domain.orm;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/** @author Steffen Jacobs */
@Repository("trackRepository")
public interface TrackRepository extends JpaRepository<TrackInfo, Long> {

	public List<TrackInfo> findByTitleContaining(String title);

	public List<TrackInfo> findByArtistContaining(String artist);

	public List<TrackInfo> findByArtistContainingOrTitleContainingOrAlbumContainingOrComposerContainingOrCommentContainingOrArtistSortContainingOrGenreContaining(String artist,
			String title, String album, String composer, String comment, String artistSort, String genre);

}
