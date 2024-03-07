package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.PathUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.PathUtils.REGISTRATION_CONFIRM_SUFFIX;
import static com.archipio.iamservice.util.PathUtils.REGISTRATION_SUFFIX;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.CREATED;

import com.archipio.iamservice.dto.RegistrationDto;
import com.archipio.iamservice.exception.NullTokenException;
import com.archipio.iamservice.service.RegistrationService;
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
@Tag(name = "Registration Controller", description = "Эндпоинты для регистрации пользователей")
public class RegistrationController {

  private final RegistrationService registrationService;

  @PostMapping(REGISTRATION_SUFFIX)
  @Operation(
      summary = "Регистрация пользователя",
      description =
          "Проверяет имя пользователя, email и пароль пользователя и при успехе отправляет письмо на почту")
  public ResponseEntity<Void> register(@Valid @RequestBody RegistrationDto registrationDto) {
    registrationService.register(registrationDto);
    return ResponseEntity.status(ACCEPTED).build();
  }

  @GetMapping(REGISTRATION_CONFIRM_SUFFIX)
  @Operation(
      summary = "Подтверждение регистрации пользователя",
      description = "Проверяет токен подтверждения и при успехе сохраняет пользователя")
  public ResponseEntity<Void> confirmRegistration(
      @Parameter(name = "token", description = "Confirmation token", required = true)
          @RequestParam("token")
          String token) {
    if (token == null || token.isEmpty()) {
      throw new NullTokenException();
    }
    registrationService.confirmRegistration(token);
    return ResponseEntity.status(CREATED).build();
  }
}
