package com.archipio.iamservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.service.JwtService;
import com.archipio.iamservice.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthenticationServiceImplTest {

  @Mock JwtService jwtService;

  @InjectMocks AuthenticationServiceImpl authenticationService;

  @Test
  public void refresh_invalidToken_thrownInvalidOrExpiredTokenException() {
    // Prepare
    final var token = "JWT token";
    when(jwtService.validate(any(String.class))).thenReturn(false);

    // Do and Check
    assertThatExceptionOfType(InvalidOrExpiredConfirmationTokenException.class)
        .isThrownBy(() -> authenticationService.refresh(token));
  }
}
