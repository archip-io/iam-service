package com.archipio.iamservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.iamservice.dto.ConfirmationTokenDto;
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

public class ConfirmationTokenDtoTest {

  private Validator validator;

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provideInvalidConfirmationTokenDto")
  public void validate_invalidConfirmationTokenDto_violationsIsNotEmpty(
      ConfirmationTokenDto confirmationTokenDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(confirmationTokenDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }

  private static Stream<Arguments> provideInvalidConfirmationTokenDto() {
    return Stream.of(
        Arguments.of(ConfirmationTokenDto.builder().token(null).build(), Set.of("token")));
  }
}
