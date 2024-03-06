package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.ApiUtils.AUTHENTICATION_SUFFIX;
import static com.archipio.iamservice.util.ApiUtils.REFRESH_TOKENS_SUFFIX;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.JwtTokensDto;
import com.archipio.iamservice.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(
    name = "Authentication Controller",
    description = "Эндпоинты для аутентификации пользователей и обновления их JWT токенов")
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @PostMapping(AUTHENTICATION_SUFFIX)
  @Operation(
      summary = "Аутентификация пользователя",
      description = "Проверяет логин и пароль пользователя и при успехе возвращает JWT токены")
  public ResponseEntity<JwtTokensDto> authenticate(
      @Valid @RequestBody AuthenticationDto authenticationDto) {
    var jwtTokensDto = authenticationService.authenticate(authenticationDto);
    return ResponseEntity.status(OK).body(jwtTokensDto);
  }

  @GetMapping(REFRESH_TOKENS_SUFFIX)
  @Operation(
      summary = "Обновление JWT токенов",
      description = "Проверяет валидность токена обновления и при успехе возвращает JWT токены")
  public ResponseEntity<JwtTokensDto> refreshTokens(
      @Parameter(name = "token", description = "JWT refresh token", required = true)
          @RequestParam("token")
          String token) {
    var jwtTokensDto = authenticationService.refresh(token);
    return ResponseEntity.status(OK).body(jwtTokensDto);
  }
}
