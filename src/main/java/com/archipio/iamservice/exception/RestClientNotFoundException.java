package com.archipio.iamservice.exception;

public class RestClientNotFoundException extends RuntimeException {

  public RestClientNotFoundException(String message) {
    super(message);
  }
}
