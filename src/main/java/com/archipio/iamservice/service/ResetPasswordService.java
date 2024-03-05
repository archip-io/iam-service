package com.archipio.iamservice.service;

import com.archipio.iamservice.dto.ResetPasswordDto;
import com.archipio.iamservice.dto.ResetPasswordSubmitDto;

public interface ResetPasswordService {

  void resetPassword(ResetPasswordDto resetPasswordDto);

  void submitPasswordReset(ResetPasswordSubmitDto resetPasswordSubmitDto);
}
