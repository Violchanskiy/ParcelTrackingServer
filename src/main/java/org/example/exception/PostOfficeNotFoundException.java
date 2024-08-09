package org.example.exception;

public class PostOfficeNotFoundException extends Exception {
  public PostOfficeNotFoundException() {
    super("Post Office Not Found");
  }
}
