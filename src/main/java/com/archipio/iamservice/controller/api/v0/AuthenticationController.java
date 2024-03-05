package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.ApiUtils.AUTHENTICATION_SUFFIX;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.JwtTokensDto;
import com.archipio.iamservice.service.AuthenticationService;
import com.archipio.iamservice.service.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V0_PREFIX)
public class AuthenticationController {

  private final AuthenticationService authenticationService;
  private final JwtService jwtService;

  @PostMapping(AUTHENTICATION_SUFFIX)
  public ResponseEntity<JwtTokensDto> authenticate(
      @Valid @RequestBody AuthenticationDto authenticationDto) {
    var credentialsWithAuthoritiesDto = authenticationService.authenticate(authenticationDto);
    var jwtTokensDto = jwtService.createTokens(credentialsWithAuthoritiesDto);
    return ResponseEntity.status(OK).body(jwtTokensDto);
  }
}
