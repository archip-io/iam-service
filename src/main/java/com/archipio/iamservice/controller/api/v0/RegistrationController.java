package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.ApiUtils.REGISTRATION_CONFIRM_SUFFIX;
import static com.archipio.iamservice.util.ApiUtils.REGISTRATION_SUFFIX;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

import com.archipio.iamservice.dto.RegistrationDto;
import com.archipio.iamservice.exception.NullTokenException;
import com.archipio.iamservice.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V0_PREFIX)
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping(REGISTRATION_SUFFIX)
  public ResponseEntity<Void> register(@Valid @RequestBody RegistrationDto registrationDto) {
    registrationService.register(registrationDto);
    return ResponseEntity.status(ACCEPTED).build();
  }

  @GetMapping(REGISTRATION_CONFIRM_SUFFIX)
  public ResponseEntity<Void> confirmRegistration(@RequestParam("token") String token) {
    if (token == null) {
      throw new NullTokenException();
    }
    registrationService.confirmRegistration(token);
    return ResponseEntity.status(CREATED).build();
  }
}
