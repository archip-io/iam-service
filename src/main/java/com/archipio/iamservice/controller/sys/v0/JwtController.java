package com.archipio.iamservice.controller.sys.v0;

import static com.archipio.iamservice.util.PathUtils.SYS_V0_PREFIX;
import static com.archipio.iamservice.util.PathUtils.VALIDATE_TOKEN_SUFFIX;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.iamservice.exception.InvalidOrExpiredJwtTokenException;
import com.archipio.iamservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(SYS_V0_PREFIX)
public class JwtController {

  private final JwtService jwtService;

  @GetMapping(VALIDATE_TOKEN_SUFFIX)
  public ResponseEntity<Void> validateToken(@RequestParam("token") String token) {
    if (!jwtService.validate(token)) {
      throw new InvalidOrExpiredJwtTokenException();
    }
    return ResponseEntity.status(OK).build();
  }
}
