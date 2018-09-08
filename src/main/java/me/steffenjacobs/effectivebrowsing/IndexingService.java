package me.steffenjacobs.effectivebrowsing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivebrowsing.domain.IndexingStatusDTO;
import me.steffenjacobs.effectivebrowsing.domain.IndexingStatusDTO.IndexingStatus;
import me.steffenjacobs.effectivebrowsing.domain.orm.TrackInfo;
import me.steffenjacobs.effectivebrowsing.domain.orm.TrackRepository;
import me.steffenjacobs.effectivebrowsing.parser.ParsingService;
import me.steffenjacobs.effectivebrowsing.util.FileUtilService;
import me.steffenjacobs.effectivebrowsing.util.Pair;

/** @author Steffen Jacobs */

@Component
public class IndexingService {
	private static final Logger LOG = LoggerFactory.getLogger(IndexingService.class);

	private final AtomicLong alreadyIndexed = new AtomicLong(0);
	private long toIndex = 0;
	private long numDirectories = 0;
	private boolean running = false;
	private final Collection<TagFail> tagFails = new CopyOnWriteArrayList<>();

	@Autowired
	FileUtilService fileUtilService;

	@Autowired
	ParsingService parsingService;

	public static class TagFail {
		final String message, path;

		public String getMessage() {
			return message;
		}

		public String getPath() {
			return path;
		}

		public TagFail(String message, String path) {
			super();
			this.message = message;
			this.path = path;
		}
	}

	public static class AggregatedError {
		final String message;
		long count;

		public long getCount() {
			return count;
		}

		public void setCount(long count) {
			this.count = count;
		}

		public String getMessage() {
			return message;
		}

		public AggregatedError(String message, long count) {
			super();
			this.message = message;
			this.count = count;
		}

	}

	@Autowired
	TrackRepository trackRepository;

	public IndexingStatusDTO startIndexing(String path) throws Exception {
		if (running) {
			throw new Exception("Already performing an import!");
		}
		alreadyIndexed.set(0);
		tagFails.clear();
		File f = new File(path);
		if (!f.exists()) {
			throw new FileNotFoundException("Could not find " + path);
		}
		new Thread(() -> {
			try {
				running = true;
				startWorker(Paths.get(path, ""));
			} catch (IOException e) {
				LOG.error(e.getMessage());
			}
		}).start();
		return new IndexingStatusDTO(IndexingStatus.STARTING);
	}

	private void startWorker(Path directory) throws IOException {
		Pair<Long, Long> countResult = fileUtilService.countFilesWithAccess(directory, tagFails::add);
		toIndex = countResult.getX() - countResult.getY();
		numDirectories = countResult.getY();

		fileUtilService.indexThoseWithAccess(directory, this::index, tagFails::add);
		running = false;

		System.out.println(tagFails.size() + " failures.");
		getErrors().forEach(e -> System.out.println(e.getMessage() + "(" + e.getCount() + ")"));
	}

	private Collection<AggregatedError> getErrors() {
		List<AggregatedError> errors = tagFails.stream().collect(Collectors.groupingBy(t -> t.getMessage(), Collectors.counting())).entrySet().stream()
				.map(e -> new AggregatedError(e.getKey(), e.getValue())).collect(Collectors.toList());
		long countNoAudioHeader = errors.stream().filter(e -> e.getMessage().startsWith("No audio header found within")).collect(Collectors.counting());
		long countInvalidIdentifier = errors.stream().filter(e -> e.getMessage().contains("Unable to find next atom because identifier is invalid")).collect(Collectors.counting());
		errors = errors.stream().filter(this::filter).collect(Collectors.toList());
		errors.add(new AggregatedError("No audio header found", countNoAudioHeader));
		errors.add(new AggregatedError("Invalid identifier", countInvalidIdentifier));
		return errors;
	}

	private boolean filter(AggregatedError error) {
		return !error.getMessage().startsWith("No audio header found within") && !error.getMessage().contains("Unable to find next atom because identifier is invalid");
	}

	public IndexingStatusDTO getStatus() {
		return new IndexingStatusDTO(toIndex, alreadyIndexed.get(), numDirectories, running ? IndexingStatus.INDEXING : IndexingStatus.FINISHED);
	}

	public TrackInfo getTrackInfo(Path path) {
		try {
			AudioFile f = AudioFileIO.read(path.toFile());
			final Tag tag = f.getTag();
			if (tag == null) {
				tagFails.add(new TagFail("Could not read file", path.toString()));
				return new TrackInfo();
			}
			return new TrackInfo(tag, f.getAudioHeader().getTrackLength() * 1000, f.getAudioHeader().getBitRateAsNumber(), parsingService.parseYear(tag),
					parsingService.parseCreationDate(path), path.toAbsolutePath().toString());
		} catch (CannotReadException | IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException e) {
			tagFails.add(new TagFail(e.getMessage(), path.toString()));
		}
		return new TrackInfo();
	}

	private void index(Path file) {
		final TrackInfo trackInfo = getTrackInfo(file);
		if (!trackInfo.isEmpty()) {
			if (trackRepository.findByTitleAndArtistAndLength(trackInfo.getTitle(), trackInfo.getArtist(), trackInfo.getLength()).isEmpty()) {
				trackRepository.save(trackInfo);
			} else {
				tagFails.add(new TagFail("Duplicate", file.toString()));
			}
		}
		alreadyIndexed.incrementAndGet();
		final long indexed = alreadyIndexed.get();
		if (indexed % 100 == 0) {
			LOG.info("Progress: " + indexed + "/" + toIndex + "(" + (indexed * 100 / (double) toIndex) + "%)");
		}
	}

}
