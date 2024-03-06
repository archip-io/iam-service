package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.ResetPasswordConfirmDto;
import com.archipio.iamservice.dto.ResetPasswordDto;

public interface ResetPasswordService {

  void resetPassword(ResetPasswordDto resetPasswordDto);

  void confirmPasswordReset(String token, ResetPasswordConfirmDto resetPasswordConfirmDto);
}
