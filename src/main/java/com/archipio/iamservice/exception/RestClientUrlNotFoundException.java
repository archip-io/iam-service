package com.archipio.iamservice.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RestClientUrlNotFoundException extends RuntimeException {

  public RestClientUrlNotFoundException(String message) {
    super(message);
  }
}
