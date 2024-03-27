package com.archipio.iamservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.iamservice.dto.ResetPasswordConfirmDto;
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

public class ResetPasswordConfirmDtoTest {

  private Validator validator;

  private static Stream<Arguments> provide_InvalidResetPasswordConfirmDto() {
    return Stream.of(
        Arguments.of(ResetPasswordConfirmDto.builder().password(null).build(), Set.of("password")),
        Arguments.of(
            ResetPasswordConfirmDto.builder().password("password_10").build(), Set.of("password")),
        Arguments.of(
            ResetPasswordConfirmDto.builder().password("Password10").build(), Set.of("password")),
        Arguments.of(
            ResetPasswordConfirmDto.builder().password("Password_").build(), Set.of("password")),
        Arguments.of(
            ResetPasswordConfirmDto.builder().password("Pw_1").build(), Set.of("password")),
        Arguments.of(
            ResetPasswordConfirmDto.builder()
                .password("Password_10Password_10Password_10Password_10Password_10Password_10")
                .build(),
            Set.of("password")));
  }

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provide_InvalidResetPasswordConfirmDto")
  public void validate_whenResetPasswordConfirmDtoIsInvalid_thenViolationsIsNotEmpty(
      ResetPasswordConfirmDto resetPasswordConfirmDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(resetPasswordConfirmDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }
}
