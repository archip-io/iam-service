package com.archipio.iamservice.unittest.service.impl;

import static com.archipio.iamservice.util.CacheUtils.REGISTRATION_CACHE_TTL_S;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.archipio.iamservice.dto.RegistrationDto;
import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.service.impl.RegistrationServiceImpl;
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
public class RegistrationServiceImplTest {

  @Mock private RedisTemplate<String, RegistrationDto> redisTemplate;
  @InjectMocks private RegistrationServiceImpl registrationService;

  @Test
  public void register_validCredentialsInputDto_Nothing() {
    // Prepare
    final String username = "username";
    final String email = "user@mail.ru";
    final String password = "Password_10";
    var registrationDto =
        RegistrationDto.builder().username(username).email(email).password(password).build();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    doNothing()
        .when(mockValueOperations)
        .set(
            any(String.class),
            eq(registrationDto),
            eq(REGISTRATION_CACHE_TTL_S),
            eq(TimeUnit.SECONDS));

    // Do
    registrationService.register(registrationDto);

    // Check
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1))
        .set(
            any(String.class),
            eq(registrationDto),
            eq(REGISTRATION_CACHE_TTL_S),
            eq(TimeUnit.SECONDS));
  }

  @Test
  public void confirmRegistration_validAndNotExpiredToken_Nothing() {
    // Prepare
    final String token = "Token";
    var registrationDto = RegistrationDto.builder().build();
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.getAndDelete(any(String.class))).thenReturn(registrationDto);

    // Do
    registrationService.confirmRegistration(token);

    // Check
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1)).getAndDelete(any(String.class));
  }

  @Test
  public void confirmRegistration_invalidOrExpiredToken_thrownInvalidOrExpiredTokenException() {
    // Prepare
    final String token = "Token";
    var mockValueOperations = mock(ValueOperations.class);

    when(redisTemplate.opsForValue()).thenReturn(mockValueOperations);
    when(mockValueOperations.getAndDelete(any(String.class))).thenReturn(null);

    // Do
    assertThatExceptionOfType(InvalidOrExpiredConfirmationTokenException.class)
        .isThrownBy(() -> registrationService.confirmRegistration(token));

    // Check
    verify(redisTemplate, times(1)).opsForValue();
    verify(mockValueOperations, times(1)).getAndDelete(any(String.class));
  }
}
