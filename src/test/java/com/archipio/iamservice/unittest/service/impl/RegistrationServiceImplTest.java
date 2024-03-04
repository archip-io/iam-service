package com.archipio.iamservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.archipio.iamservice.cache.entity.CredentialsCache;
import com.archipio.iamservice.cache.repository.CredentialsRepository;
import com.archipio.iamservice.dto.CredentialsInputDto;
import com.archipio.iamservice.dto.TokenInputDto;
import com.archipio.iamservice.exception.InvalidOrExpiredTokenException;
import com.archipio.iamservice.mapper.CredentialsMapper;
import com.archipio.iamservice.service.impl.RegistrationServiceImpl;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class RegistrationServiceImplTest {

  @Mock private CredentialsRepository credentialsRepository;
  @Mock private CredentialsMapper credentialsMapper;
  @InjectMocks private RegistrationServiceImpl registrationService;

  @Test
  public void register_validCredentialsInputDto_Nothing() {
    // Prepare
    final String username = "username";
    final String email = "user@mail.ru";
    final String password = "Password_10";
    var credentialsInputDto =
        CredentialsInputDto.builder().username(username).email(email).password(password).build();
    var credentialsCache = CredentialsCache.builder().build();
    when(credentialsMapper.toCache(credentialsInputDto)).thenReturn(credentialsCache);
    when(credentialsRepository.save(credentialsCache)).thenReturn(credentialsCache);

    // Do
    registrationService.register(credentialsInputDto);

    // Check
    verify(credentialsMapper, times(1)).toCache(credentialsInputDto);
    verify(credentialsRepository, times(1)).save(credentialsCache);
  }

  @Test
  public void submitRegistration_validAndNotExpiredToken_Nothing() {
    // Prepare
    final String token = "Token";
    var tokenInputDto = TokenInputDto.builder().token(token).build();
    var credentialsCache = CredentialsCache.builder().build();
    when(credentialsRepository.findByToken(token)).thenReturn(Optional.of(credentialsCache));

    // Do
    registrationService.submitRegistration(tokenInputDto);

    // Check
    verify(credentialsRepository, times(1)).findByToken(token);
  }

  @Test
  public void submitRegistration_invalidOrExpiredToken_thrownInvalidOrExpiredTokenException() {
    // Prepare
    final String token = "Token";
    var tokenInputDto = TokenInputDto.builder().token(token).build();
    when(credentialsRepository.findByToken(token)).thenReturn(Optional.empty());

    // Do and Check
    assertThatExceptionOfType(InvalidOrExpiredTokenException.class)
        .isThrownBy(() -> registrationService.submitRegistration(tokenInputDto));
    verify(credentialsRepository, times(1)).findByToken(token);
  }
}
