package com.archipio.iamservice.unittest.service.impl;

import static com.archipio.iamservice.util.CacheUtils.RESET_PASSWORD_CACHE_TTL_S;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import com.archipio.iamservice.client.UserServiceClient;
import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.dto.ResetPasswordConfirmDto;
import com.archipio.iamservice.dto.ResetPasswordDto;
import com.archipio.iamservice.exception.BannedUserException;
import com.archipio.iamservice.exception.CredentialsNotFoundException;
import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.service.impl.ResetPasswordServiceImpl;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
public class ResetPasswordServiceImplTest {

  @Mock private RedisTemplate<String, ResetPasswordDto> redisTemplate;
  @Mock private UserServiceClient userServiceClient;
  @InjectMocks private ResetPasswordServiceImpl resetPasswordService;

  @Test
  public void resetPassword_whenCredentialsExistsAndUserIsNotBanned_thenSaveCredentialsInCache() {
    // Prepare
    final var login = "login";
    final var resetPasswordDto = ResetPasswordDto.builder().login(login).build();
    final var credentialsDto = CredentialsDto.builder().isEnabled(true).build();
    final var mockValueOperations = mock(ValueOperations.class);

    when(userServiceClient.findCredentialsByLogin(login)).thenReturn(credentialsDto);
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
    verify(userServiceClient, only()).findCredentialsByLogin(login);
    verify(redisTemplate, only()).opsForValue();
    verify(mockValueOperations, only())
        .set(
            any(String.class),
            eq(resetPasswordDto),
            eq(RESET_PASSWORD_CACHE_TTL_S),
            eq(TimeUnit.SECONDS));
  }

  @Test
  public void resetPassword_whenCredentialsNotFound_thenThrownCredentialsNotFoundException() {
    // Prepare
    final var login = "login";
    final var resetPasswordDto = ResetPasswordDto.builder().login(login).build();

    when(userServiceClient.findCredentialsByLogin(login)).thenReturn(null);

    // Do
    assertThatExceptionOfType(CredentialsNotFoundException.class)
        .isThrownBy(() -> resetPasswordService.resetPassword(resetPasswordDto));

    // Check
    verify(userServiceClient, only()).findCredentialsByLogin(login);
  }

  @Test
  public void resetPassword_whenCredentialsExistsAndUserIsBanned_thenThrownBannedUserException() {
    // Prepare
    final var login = "login";
    final var resetPasswordDto = ResetPasswordDto.builder().login(login).build();
    final var credentialsDto = CredentialsDto.builder().isEnabled(false).build();

    when(userServiceClient.findCredentialsByLogin(login)).thenReturn(credentialsDto);

    // Do
    assertThatExceptionOfType(BannedUserException.class)
        .isThrownBy(() -> resetPasswordService.resetPassword(resetPasswordDto));

    // Check
    verify(userServiceClient, only()).findCredentialsByLogin(login);
  }

  @Test
  public void confirmPasswordReset_whenTokenIsValid_thenResetPassword() {
    // Prepare
    final var token = "Token";
    final var login = "login";
    final var password = "Password_10";
    final var resetPasswordDto = ResetPasswordDto.builder().login(login).build();
    final var resetPasswordConfirmDto =
        ResetPasswordConfirmDto.builder().password(password).build();
    final var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.get(any(String.class))).thenReturn(resetPasswordDto);
    doNothing()
        .when(userServiceClient)
        .resetPassword(resetPasswordDto.getLogin(), resetPasswordConfirmDto.getPassword());
    when(mockValueOperations.getAndDelete(any(String.class))).thenReturn(resetPasswordDto);

    // Do
    resetPasswordService.confirmPasswordReset(token, resetPasswordConfirmDto);

    // Check
    verify(redisTemplate, times(2)).opsForValue();
    verify(mockValueOperations, times(1)).get(any(String.class));
    verify(userServiceClient, only())
        .resetPassword(resetPasswordDto.getLogin(), resetPasswordConfirmDto.getPassword());
    verify(mockValueOperations, times(1)).getAndDelete(any(String.class));
  }

  @Test
  public void confirmPasswordReset_whenTokenIsInvalid_thrownInvalidOrExpiredTokenException() {
    // Prepare
    final String token = "Token";
    final var resetPasswordConfirmDto = ResetPasswordConfirmDto.builder().build();
    final var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.get(any(String.class))).thenReturn(null);

    // Do
    assertThatExceptionOfType(InvalidOrExpiredConfirmationTokenException.class)
        .isThrownBy(
            () -> resetPasswordService.confirmPasswordReset(token, resetPasswordConfirmDto));

    // Check
    verify(redisTemplate, only()).opsForValue();
    verify(mockValueOperations, only()).get(any(String.class));
  }
}
