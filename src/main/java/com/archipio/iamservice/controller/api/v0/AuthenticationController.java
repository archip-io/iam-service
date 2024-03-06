package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.ApiUtils.AUTHENTICATION_SUFFIX;
import static com.archipio.iamservice.util.ApiUtils.REFRESH_TOKENS_SUFFIX;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.JwtTokensDto;
import com.archipio.iamservice.service.AuthenticationService;
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
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping(AUTHENTICATION_SUFFIX)
  public ResponseEntity<JwtTokensDto> authenticate(
      @Valid @RequestBody AuthenticationDto authenticationDto) {
    var jwtTokensDto = authenticationService.authenticate(authenticationDto);
    return ResponseEntity.status(OK).body(jwtTokensDto);
  }

  @GetMapping(REFRESH_TOKENS_SUFFIX)
  public ResponseEntity<JwtTokensDto> refreshTokens(@RequestParam("token") String token) {
    var jwtTokensDto = authenticationService.refresh(token);
    return ResponseEntity.status(OK).body(jwtTokensDto);
  }
}
