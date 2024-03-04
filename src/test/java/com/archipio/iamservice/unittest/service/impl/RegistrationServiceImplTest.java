package com.archipio.iamservice.unittest.service.impl;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.archipio.iamservice.cache.entity.CredentialsCache;
import com.archipio.iamservice.cache.repository.CredentialsRepository;
import com.archipio.iamservice.dto.CredentialsInputDto;
import com.archipio.iamservice.mapper.CredentialsMapper;
import com.archipio.iamservice.service.impl.RegistrationServiceImpl;
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
}
