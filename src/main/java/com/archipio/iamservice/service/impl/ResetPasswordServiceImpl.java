package com.archipio.iamservice.service.impl;

import static com.archipio.iamservice.util.CacheUtils.RESET_PASSWORD_CACHE_TTL_S;

import com.archipio.iamservice.dto.ResetPasswordConfirmDto;
import com.archipio.iamservice.dto.ResetPasswordDto;
import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.service.ResetPasswordService;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetPasswordServiceImpl implements ResetPasswordService {

  private static final String RESET_PASSWORD_KEY_PREFIX = "service:reset-password:";

  private final RedisTemplate<String, ResetPasswordDto> redisTemplate;

  @Override
  public void resetPassword(ResetPasswordDto resetPasswordDto) {
    // TODO: Проверить есть ли такой логин в UserService

    var token = UUID.randomUUID().toString();
    redisTemplate
        .opsForValue()
        .set(
            RESET_PASSWORD_KEY_PREFIX + token,
            resetPasswordDto,
            RESET_PASSWORD_CACHE_TTL_S,
            TimeUnit.SECONDS);

    // TODO: Добавить в Kafka событие отправки письма
  }

  @Override
  public void confirmPasswordReset(String token, ResetPasswordConfirmDto resetPasswordConfirmDto) {
    var resetPasswordDto =
        redisTemplate.opsForValue().getAndDelete(RESET_PASSWORD_KEY_PREFIX + token);
    if (resetPasswordDto == null) {
      throw new InvalidOrExpiredConfirmationTokenException();
    }
  }
}
