package com.archipio.iamservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.iamservice.dto.ResetPasswordSubmitDto;
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

public class ResetPasswordSubmitDtoTest {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provideInvalidResetPasswordSubmitDto")
  public void validate_invalidResetPasswordSubmitDto_violationsIsNotEmpty(
      ResetPasswordSubmitDto resetPasswordSubmitDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(resetPasswordSubmitDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }

  private static Stream<Arguments> provideInvalidResetPasswordSubmitDto() {
    return Stream.of(
        Arguments.of(
            ResetPasswordSubmitDto.builder().token(null).password("Password_10").build(),
            Set.of("token")),
        Arguments.of(
            ResetPasswordSubmitDto.builder().token("Token").password(null).build(),
            Set.of("password")),
        Arguments.of(
            ResetPasswordSubmitDto.builder().token("Token").password("password_10").build(),
            Set.of("password")),
        Arguments.of(
            ResetPasswordSubmitDto.builder().token("Token").password("Password10").build(),
            Set.of("password")),
        Arguments.of(
            ResetPasswordSubmitDto.builder().token("Token").password("Password_").build(),
            Set.of("password")),
        Arguments.of(
            ResetPasswordSubmitDto.builder().token("Token").password("Pw_1").build(),
            Set.of("password")),
        Arguments.of(
            ResetPasswordSubmitDto.builder()
                .token("Token")
                .password("Password_10Password_10Password_10Password_10Password_10Password_10")
                .build(),
            Set.of("password")));
  }
}
