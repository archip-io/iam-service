package com.archipio.iamservice.service.impl;

import com.archipio.iamservice.cache.repository.CredentialsRepository;
import com.archipio.iamservice.dto.CredentialsInputDto;
import com.archipio.iamservice.mapper.CredentialsMapper;
import com.archipio.iamservice.service.RegistrationService;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

  private final CredentialsRepository credentialsRepository;
  private final CredentialsMapper credentialsMapper;

  @Override
  public void register(@Valid CredentialsInputDto inputDto) {
    // TODO: Проверить есть ли такие учётные данные в User Service

    var credentialsCache = credentialsMapper.toCache(inputDto);
    credentialsCache.setToken(UUID.randomUUID().toString());
    credentialsRepository.save(credentialsCache);

    // TODO: Добавить в Kafka событие отправки письма
  }
}
