package com.hussain.pink.triangle.filewatcher;

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

  public void watchDropFolder() {
    LOG.info("Watching the folder {}", this.dropDirectory);
  }

}
