package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.PathUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.PathUtils.RESET_PASSWORD_CONFIRM_SUFFIX;
import static com.archipio.iamservice.util.PathUtils.RESET_PASSWORD_SUFFIX;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.HttpStatus.OK;

import com.archipio.iamservice.dto.ResetPasswordConfirmDto;
import com.archipio.iamservice.dto.ResetPasswordDto;
import com.archipio.iamservice.exception.NullConfirmationTokenException;
import com.archipio.iamservice.service.ResetPasswordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(API_V0_PREFIX)
public class ResetPasswordController {

  private final ResetPasswordService resetPasswordService;

  @PatchMapping(RESET_PASSWORD_SUFFIX)
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
    resetPasswordService.resetPassword(resetPasswordDto);
    return ResponseEntity.status(ACCEPTED).build();
  }

  @PatchMapping(RESET_PASSWORD_CONFIRM_SUFFIX)
  public ResponseEntity<Void> confirmRegistration(
      @RequestParam("token") String token,
      @Valid @RequestBody ResetPasswordConfirmDto resetPasswordConfirmDto) {
    if (token == null || token.isEmpty()) {
      throw new NullConfirmationTokenException();
    }
    resetPasswordService.confirmPasswordReset(token, resetPasswordConfirmDto);
    return ResponseEntity.status(OK).build();
  }
}
