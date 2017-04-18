package com.hussain.pink.triangle.exception;

import java.io.IOException;

/**
 * Created by Hussain on 04/04/2017.
 */
public class FileWatcherException extends IOException {

  public FileWatcherException(String message) {
    super(message);
  }

  public FileWatcherException(String message, Throwable cause) {
    super(message, cause);
  }

}
