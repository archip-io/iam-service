package com.archipio.iamservice.service.impl;

import static com.archipio.iamservice.util.CacheUtils.REGISTRATION_CACHE_TTL_S;

import com.archipio.iamservice.dto.RegistrationDto;
import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.service.RegistrationService;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

  private static final String REGISTRATION_KEY_PREFIX = "service:registration:";

  private final RedisTemplate<String, RegistrationDto> redisTemplate;
  private final BCryptPasswordEncoder passwordEncoder;

  @Override
  public void register(RegistrationDto registrationDto) {
    // TODO: Проверить есть ли такие учётные данные в User Service

    registrationDto.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

    var token = UUID.randomUUID().toString();
    redisTemplate
        .opsForValue()
        .set(
            REGISTRATION_KEY_PREFIX + token,
            registrationDto,
            REGISTRATION_CACHE_TTL_S,
            TimeUnit.SECONDS);

    // TODO: Добавить в Kafka событие отправки письма
  }

  @Override
  public void confirmRegistration(String token) {
    var registrationDto = redisTemplate.opsForValue().getAndDelete(REGISTRATION_KEY_PREFIX + token);
    if (registrationDto == null) {
      throw new InvalidOrExpiredConfirmationTokenException();
    }

    // TODO: Создать учётные данные в User Service
  }
}
