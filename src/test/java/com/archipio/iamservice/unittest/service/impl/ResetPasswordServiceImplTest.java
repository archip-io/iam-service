package com.archipio.iamservice.unittest.service.impl;

import static com.archipio.iamservice.util.CacheUtils.RESET_PASSWORD_CACHE_TTL_S;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.archipio.iamservice.dto.ResetPasswordConfirmDto;
import com.archipio.iamservice.dto.ResetPasswordDto;
import com.archipio.iamservice.exception.InvalidOrExpiredTokenException;
import com.archipio.iamservice.service.impl.ResetPasswordServiceImpl;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ResetPasswordServiceImplTest {

  @Mock private RedisTemplate<String, ResetPasswordDto> redisTemplate;
  @InjectMocks private ResetPasswordServiceImpl resetPasswordService;

  @Test
  public void resetPassword_validResetPasswordDto_Nothing() {
    // Prepare
    final String login = "username";
    var resetPasswordDto = ResetPasswordDto.builder().login(login).build();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    doNothing()
        .when(mockValueOperations)
        .set(
            any(String.class),
            eq(resetPasswordDto),
            eq(RESET_PASSWORD_CACHE_TTL_S),
            eq(TimeUnit.SECONDS));

    // Do
    resetPasswordService.resetPassword(resetPasswordDto);

    // Check
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1))
        .set(
            any(String.class),
            eq(resetPasswordDto),
            eq(RESET_PASSWORD_CACHE_TTL_S),
            eq(TimeUnit.SECONDS));
  }

  @Test
  public void confirmPasswordReset_validAndNotExpiredToken_Nothing() {
    // Prepare
    final String token = "Token";
    final String password = "Password_10";
    var resetPasswordConfirmDto = ResetPasswordConfirmDto.builder().build();
    var resetPasswordDto = ResetPasswordDto.builder().build();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.getAndDelete(any(String.class))).thenReturn(resetPasswordDto);

    // Do
    resetPasswordService.confirmPasswordReset(token, resetPasswordConfirmDto);

    // Check
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1)).getAndDelete(any(String.class));
  }

  @Test
  public void confirmPasswordReset_invalidOrExpiredToken_thrownInvalidOrExpiredTokenException() {
    // Prepare
    final String token = "Token";
    final String password = "Password_10";
    var resetPasswordConfirmDto = ResetPasswordConfirmDto.builder().build();
    var resetPasswordDto = ResetPasswordDto.builder().build();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.getAndDelete(any(String.class))).thenReturn(null);

    // Do
    assertThatExceptionOfType(InvalidOrExpiredTokenException.class)
        .isThrownBy(
            () -> resetPasswordService.confirmPasswordReset(token, resetPasswordConfirmDto));

    // Check
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1)).getAndDelete(any(String.class));
  }
}
