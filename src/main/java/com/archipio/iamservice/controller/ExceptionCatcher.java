package com.archipio.iamservice.controller;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.archipio.iamservice.dto.ErrorDto;
import com.archipio.iamservice.exception.BannedUserException;
import com.archipio.iamservice.exception.CredentialsAlreadyExistsException;
import com.archipio.iamservice.exception.CredentialsNotFoundException;
import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.exception.InvalidOrExpiredJwtTokenException;
import com.archipio.iamservice.exception.NullTokenException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.support.RequestContextUtils;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionCatcher {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      HttpServletRequest request, MethodArgumentNotValidException e) {
    var errors =
        e.getBindingResult().getFieldErrors().stream()
            .collect(
                Collectors.groupingBy(
                    FieldError::getField,
                    Collectors.mapping(FieldError::getDefaultMessage, Collectors.joining(" "))));

    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.validation-error", request))
                .errors(errors)
                .build());
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorDto> handleNoHandlerFoundException(HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.endpoint-not-found", request))
                .build());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleHttpRequestMethodNotSupportedException(
      HttpServletRequest request) {
    return ResponseEntity.status(METHOD_NOT_ALLOWED)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.method-not-supported", request))
                .build());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(
      HttpServletRequest request) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.invalid-json-format", request))
                .build());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handleMissingServletRequestParameterException(
      HttpServletRequest request) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.missing-request-parameter", request))
                .build());
  }

  @ExceptionHandler(CredentialsAlreadyExistsException.class)
  public ResponseEntity<ErrorDto> handleCredentialAlreadyExistsException(
      HttpServletRequest request) {
    return ResponseEntity.status(CONFLICT)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.credentials-already-exists", request))
                .build());
  }

  @ExceptionHandler(CredentialsNotFoundException.class)
  public ResponseEntity<ErrorDto> handleCredentialsNotFoundException(HttpServletRequest request) {
    return ResponseEntity.status(NOT_FOUND)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.credentials-not-found", request))
                .build());
  }

  @ExceptionHandler(BannedUserException.class)
  public ResponseEntity<ErrorDto> handleBannedUserException(HttpServletRequest request) {
    return ResponseEntity.status(FORBIDDEN)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.banned-user", request))
                .build());
  }

  @ExceptionHandler(InvalidOrExpiredConfirmationTokenException.class)
  public ResponseEntity<ErrorDto> handleInvalidOrExpiredTokenException(HttpServletRequest request) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.invalid-or-expired-confirmation-token", request))
                .build());
  }

  @ExceptionHandler(InvalidOrExpiredJwtTokenException.class)
  public ResponseEntity<ErrorDto> handleInvalidOrExpiredJwtTokenException(
      HttpServletRequest request) {
    return ResponseEntity.status(UNAUTHORIZED)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.invalid-or-expired-jwt-token", request))
                .build());
  }

  @ExceptionHandler(NullTokenException.class)
  public ResponseEntity<ErrorDto> handleNullTokenException(HttpServletRequest request) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.null-token", request))
                .build());
  }

  @ExceptionHandler(HttpClientErrorException.class)
  public ResponseEntity<String> handleHttpClientErrorException(HttpClientErrorException e) {
    var code = e.getStatusCode();
    var body = e.getResponseBodyAsString();
    return ResponseEntity.status(code).body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(HttpServletRequest request) {
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(getMessage("exception.internal-server-error", request))
                .build());
  }

  private String getMessage(String code, HttpServletRequest request) {
    return messageSource.getMessage(code, null, RequestContextUtils.getLocale(request));
  }
}
