package com.archipio.iamservice.controller.sys.v0;

import static com.archipio.iamservice.util.PathUtils.SYS_V0_PREFIX;
import static com.archipio.iamservice.util.PathUtils.VALIDATE_TOKEN_SUFFIX;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import com.archipio.iamservice.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(SYS_V0_PREFIX)
@Tag(name = "JWT Controller", description = "Эндпоинты для проверки JWT токенов")
public class JwtController {

  private final JwtService jwtService;

  @GetMapping(VALIDATE_TOKEN_SUFFIX)
  @Operation(
      summary = "Валидация токена доступа",
      description = "Проверяет подпись и срок действия JWT токена доступа")
  public ResponseEntity<Void> validateToken(
      @Parameter(name = "token", description = "JWT access token", required = true)
          @RequestParam("token")
          String token) {
    if (jwtService.validate(token)) {
      return ResponseEntity.status(OK).build();
    }
    return ResponseEntity.status(UNAUTHORIZED).build();
  }
}
