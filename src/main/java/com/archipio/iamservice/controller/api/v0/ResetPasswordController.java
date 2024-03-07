package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.ApiUtils.RESET_PASSWORD_CONFIRM_SUFFIX;
import static com.archipio.iamservice.util.ApiUtils.RESET_PASSWORD_SUFFIX;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.iamservice.dto.ResetPasswordConfirmDto;
import com.archipio.iamservice.dto.ResetPasswordDto;
import com.archipio.iamservice.exception.NullTokenException;
import com.archipio.iamservice.service.ResetPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V0_PREFIX)
@Tag(name = "Reset Password Controller", description = "Эндпоинты для сброса паролей пользователей")
public class ResetPasswordController {

  private final ResetPasswordService resetPasswordService;

  @PutMapping(RESET_PASSWORD_SUFFIX)
  @Operation(
      summary = "Сброс пароля пользователя",
      description = "Проверяет логин пользователя и при успехе отправляет письмо на почту")
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
    resetPasswordService.resetPassword(resetPasswordDto);
    return ResponseEntity.status(ACCEPTED).build();
  }

  @PutMapping(RESET_PASSWORD_CONFIRM_SUFFIX)
  @Operation(
      summary = "Подтверждение сброса пароля и обновление пароля",
      description =
          "Проверяет токен подтверждения и новый пароль пользователя и при успехе обновляет пароль")
  public ResponseEntity<Void> confirmRegistration(
      @Parameter(name = "token", description = "Confirmation token", required = true)
          @RequestParam("token")
          String token,
      @RequestBody ResetPasswordConfirmDto resetPasswordConfirmDto) {
    if (token == null || token.isEmpty()) {
      throw new NullTokenException();
    }
    resetPasswordService.confirmPasswordReset(token, resetPasswordConfirmDto);
    return ResponseEntity.status(OK).build();
  }
}
