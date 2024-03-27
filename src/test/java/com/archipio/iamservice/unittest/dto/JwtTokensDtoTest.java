package com.archipio.iamservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.iamservice.dto.JwtTokensDto;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

@JsonTest
public class JwtTokensDtoTest {

  @Autowired private JacksonTester<JwtTokensDto> jwtTokensDtoJson;

  @Test
  public void checkJwtTokensDtoTestWithoutErrors() throws IOException {
    // Prepare
    final String accessToken = "Access Token";
    final String refreshToken = "Refresh Token";
    JwtTokensDto jwtTokensDto =
        JwtTokensDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();

    // Do
    JsonContent<JwtTokensDto> result = jwtTokensDtoJson.write(jwtTokensDto);

    // Check
    assertThat(result).hasJsonPathStringValue("$.access_token");
    assertThat(result).extractingJsonPathStringValue("$.access_token").isEqualTo(accessToken);
    assertThat(result).hasJsonPathStringValue("$.refresh_token");
    assertThat(result).extractingJsonPathStringValue("$.refresh_token").isEqualTo(refreshToken);
  }
}
