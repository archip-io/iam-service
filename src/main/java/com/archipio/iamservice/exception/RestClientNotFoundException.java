package com.archipio.iamservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestClientNotFoundException extends RuntimeException {

  public RestClientNotFoundException(String message) {
    super(message);
  }
}
