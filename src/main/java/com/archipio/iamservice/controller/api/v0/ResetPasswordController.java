package com.archipio.iamservice.controller.api.v0;

import static com.archipio.iamservice.util.ApiUtils.API_V0_PREFIX;
import static com.archipio.iamservice.util.ApiUtils.RESET_PASSWORD_SUFFIX;
import static org.springframework.http.HttpStatus.ACCEPTED;

import com.archipio.iamservice.dto.ResetPasswordDto;
import com.archipio.iamservice.service.ResetPasswordService;
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
public class ResetPasswordController {

  private final ResetPasswordService resetPasswordService;

  @PostMapping(RESET_PASSWORD_SUFFIX)
  public ResponseEntity<Void> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
    resetPasswordService.resetPassword(resetPasswordDto);
    return ResponseEntity.status(ACCEPTED).build();
  }
}
