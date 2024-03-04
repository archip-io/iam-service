package com.archipio.iamservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.iamservice.dto.CredentialsInputDto;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CredentialDtoTest {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provideInvalidCredentials")
  public void validate_invalidCredentialsInputDto_violationsIsNotEmpty(
      CredentialsInputDto credentialsInputDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(credentialsInputDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsAll(expectedErrorFields);
  }

  private static Stream<Arguments> provideInvalidCredentials() {
    return Stream.of(
        Arguments.of(
            CredentialsInputDto.builder()
                .username(null)
                .email("user@mail.ru")
                .password("Password_10")
                .build(),
            Set.of("username")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("")
                .email("user@mail.ru")
                .password("Password_10")
                .build(),
            Set.of("username")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("1")
                .email("user@mail.ru")
                .password("Password_10")
                .build(),
            Set.of("username")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("useruseruseruseruseruseruseruser")
                .email("user@mail.ru")
                .password("Password_10")
                .build(),
            Set.of("username")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("user user")
                .email("user@mail.ru")
                .password("Password_10")
                .build(),
            Set.of("username")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("username")
                .email(null)
                .password("Password_10")
                .build(),
            Set.of("email")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("username")
                .email("usermail.ru")
                .password("Password_10")
                .build(),
            Set.of("email")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("username")
                .email("user@mail.ru")
                .password(null)
                .build(),
            Set.of("password")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("username")
                .email("user@mail.ru")
                .password("")
                .build(),
            Set.of("password")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("username")
                .email("user@mail.ru")
                .password("Password_10Password_10Password_10Password_10Password_10Password_10")
                .build(),
            Set.of("password")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("username")
                .email("user@mail.ru")
                .password("password_10")
                .build(),
            Set.of("password")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("username")
                .email("user@mail.ru")
                .password("Password10")
                .build(),
            Set.of("password")),
        Arguments.of(
            CredentialsInputDto.builder()
                .username("username")
                .email("user@mail.ru")
                .password("Password_")
                .build(),
            Set.of("password")));
  }
}
