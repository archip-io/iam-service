package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.ApiUtils.REGISTRATION_SUBMIT_SUFFIX;
import static com.archipio.iamservice.util.ApiUtils.REGISTRATION_SUFFIX;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

import com.archipio.iamservice.dto.CredentialsInputDto;
import com.archipio.iamservice.dto.TokenInputDto;
import com.archipio.iamservice.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V0_PREFIX)
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping(REGISTRATION_SUFFIX)
  public ResponseEntity<Void> register(@RequestBody CredentialsInputDto inputDto) {
    registrationService.register(inputDto);
    return ResponseEntity.status(ACCEPTED).build();
  }

  @PostMapping(REGISTRATION_SUBMIT_SUFFIX)
  public ResponseEntity<Void> submitRegistration(@RequestBody TokenInputDto inputDto) {
    registrationService.submitRegistration(inputDto);
    return ResponseEntity.status(CREATED).build();
  }
}
