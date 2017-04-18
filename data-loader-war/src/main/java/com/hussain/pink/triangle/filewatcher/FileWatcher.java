package com.hussain.pink.triangle.filewatcher;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

import com.hussain.pink.triangle.exception.FileWatcherException;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Hussain on 04/04/2017.
 */
public class FileWatcher {

  private static final Logger LOG = LoggerFactory.getLogger(FileWatcher.class);

  private final String dropDirectory;


  public FileWatcher(final String dropDirectory) {
    this.dropDirectory = dropDirectory;
  }

  public void watchDropFolder() throws FileWatcherException {
    LOG.info("Watching the folder {} for the creation of new files", this.dropDirectory);

    final Path dropFolder = validateDirectoryPath().toPath();

    final FileSystem fileSystem = dropFolder.getFileSystem();

    try (final WatchService watchService = fileSystem.newWatchService()) {

      //We only register creation events
      dropFolder.register(watchService, ENTRY_CREATE);

      WatchKey watchKey;

      while (true) {
        watchKey = watchService.take();

        Kind<?> kind;
        for (WatchEvent<?> watchEvent : watchKey.pollEvents()) {
          kind = watchEvent.kind();

          if (kind == ENTRY_CREATE) {
            Path newFileDrop = ((WatchEvent<Path>) watchEvent).context();
            LOG.info("A new file has been dropped: {}", newFileDrop.toFile());
          }
        }
      }

    } catch (IOException ioException) {
      LOG.error("There was an error with the watch service", ioException);
      throw new FileWatcherException("There was an error with the watch service", ioException);
    } catch (InterruptedException interruptedException) {
      LOG.error("There was an error when getting the event of the watcher", interruptedException);
      throw new FileWatcherException("There was an error with the watcher", interruptedException);
    }

  }


  /**
   * We validate if the file path that was passed in the properties file is
   * a valid directory to watch. If the file path passes all validation
   * then this method returns a valid <code>File</code> object that represents the
   * folder to watch
   *
   * @return A file object representing the folder to watch
   * @throws FileWatcherException If there is a validation error
   */
  private File validateDirectoryPath() throws FileWatcherException {
    final File folderToWatch = new File(this.dropDirectory);

    //Check if the path represents a directory
    if (!folderToWatch.isDirectory()) {
      LOG.error("The path {} does not represent a valid directory");
      throw new FileWatcherException("The drop directory is not a directory");
    }

    //Check if the directory exists
    if (!folderToWatch.exists()) {
      LOG.error("The directory {} does not exist");
      throw new FileWatcherException("The drop directory does not exist");
    }

    //The path has passed all the validation checks -
    //return it as a file object
    return folderToWatch;
  }

}
