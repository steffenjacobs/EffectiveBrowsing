package me.steffenjacobs.effectivebrowsing.util;

import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import me.steffenjacobs.effectivebrowsing.IndexingService.TagFail;
import me.steffenjacobs.effectivebrowsing.Pair;

/** @author Steffen Jacobs */

@Component
public class FileUtilService {
	public Pair<Long, Long> countFilesWithAccess(Path path, Consumer<TagFail> errorConsumer) throws IOException {

		AtomicLong totalCount = new AtomicLong(0);
		AtomicLong directoryCount = new AtomicLong(0);
		Files.walkFileTree(path, new HashSet<FileVisitOption>(Arrays.asList(FileVisitOption.FOLLOW_LINKS)), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				totalCount.incrementAndGet();
				if (attrs.isDirectory()) {
					directoryCount.incrementAndGet();
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
				errorConsumer.accept(new TagFail(e.getMessage(), file.toString()));
				return FileVisitResult.SKIP_SUBTREE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});

		return new Pair<>(totalCount.get(), directoryCount.get());
	}

	public Pair<Long, Long> fileCount(Path dir) throws IOException {
		long totalCount = Files.walk(dir).parallel().count();
		long directoryCount = Files.walk(dir).parallel().filter(p -> p.toFile().isDirectory()).count();
		return new Pair<>(totalCount, directoryCount);
	}

	public void indexThoseWithAccess(Path path, final Consumer<Path> consumer, final Consumer<TagFail> errorConsumer) throws IOException {
		Files.walkFileTree(path, new HashSet<FileVisitOption>(Arrays.asList(FileVisitOption.FOLLOW_LINKS)), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (attrs.isRegularFile()) {
					consumer.accept(file);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException e) throws IOException {
				errorConsumer.accept(new TagFail(e.getMessage(), file.toString()));
				return FileVisitResult.SKIP_SUBTREE;
			}

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		});
	}

	public void indexNaive(Path path, Consumer<Path> consumer) throws IOException {
		Files.find(Paths.get(path.toUri()), Integer.MAX_VALUE, (filePath, fileAttr) -> fileAttr.isRegularFile()).forEach(consumer);
	}
}
