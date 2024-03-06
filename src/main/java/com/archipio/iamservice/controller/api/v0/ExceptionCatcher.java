package com.archipio.iamservice.controller.api.v0;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.archipio.iamservice.dto.ErrorDto;
import com.archipio.iamservice.exception.CredentialAlreadyExistsException;
import com.archipio.iamservice.exception.InvalidOrExpiredTokenException;
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
                .message(
                    messageSource.getMessage(
                        "exception.validation-error", null, RequestContextUtils.getLocale(request)))
                .errors(errors)
                .build());
  }

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ErrorDto> handleNoHandlerFoundException(
      HttpServletRequest request, NoHandlerFoundException e) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.endpoint-not-found",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ResponseEntity<ErrorDto> handleHttpRequestMethodNotSupportedException(
      HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
    return ResponseEntity.status(METHOD_NOT_ALLOWED)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.method-not-supported",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorDto> handleHttpMessageNotReadableException(
      HttpServletRequest request, HttpMessageNotReadableException e) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.invalid-json-format",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorDto> handleMissingServletRequestParameterException(
      HttpServletRequest request, MissingServletRequestParameterException e) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.missing-request-parameter",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(CredentialAlreadyExistsException.class)
  public ResponseEntity<ErrorDto> handleCredentialAlreadyExistsException(
      HttpServletRequest request, CredentialAlreadyExistsException e) {
    return ResponseEntity.status(CONFLICT)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.credentials-already-exists",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(InvalidOrExpiredTokenException.class)
  public ResponseEntity<ErrorDto> handleInvalidOrExpiredTokenException(
      HttpServletRequest request, InvalidOrExpiredTokenException e) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.invalid-or-expired-token",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(NullTokenException.class)
  public ResponseEntity<ErrorDto> handleNullTokenException(
      HttpServletRequest request, NullTokenException e) {
    return ResponseEntity.status(BAD_REQUEST)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.null-token", null, RequestContextUtils.getLocale(request)))
                .build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorDto> handleException(HttpServletRequest request, Exception e) {
    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(
            ErrorDto.builder()
                .createdAt(Instant.now())
                .message(
                    messageSource.getMessage(
                        "exception.internal-server-error",
                        null,
                        RequestContextUtils.getLocale(request)))
                .build());
  }
}
