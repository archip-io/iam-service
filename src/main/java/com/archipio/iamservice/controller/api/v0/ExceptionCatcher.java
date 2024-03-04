package com.archipio.iamservice.controller.api.v0;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

import com.archipio.iamservice.dto.ErrorDto;
import com.archipio.iamservice.exception.CredentialAlreadyExistsException;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.RequestContextUtils;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionCatcher {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorDto> handleMethodArgumentNotValidException(
      HttpServletRequest request, MethodArgumentNotValidException e) {
    log.debug("catch MethodArgumentNotValidException", e);

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

  @ExceptionHandler(CredentialAlreadyExistsException.class)
  public ResponseEntity<ErrorDto> handleCredentialAlreadyExistsException(
      HttpServletRequest request, CredentialAlreadyExistsException e) {
    log.debug("catch CredentialAlreadyExistsException", e);
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
}
