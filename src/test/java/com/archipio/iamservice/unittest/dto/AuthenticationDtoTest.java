package com.archipio.iamservice.unittest.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.archipio.iamservice.dto.AuthenticationDto;
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

public class AuthenticationDtoTest {

  private Validator validator;

  private static Stream<Arguments> provide_InvalidAuthenticationDto() {
    return Stream.of(
        Arguments.of(
            AuthenticationDto.builder().login(null).password("Password_10").build(),
            Set.of("login")),
        Arguments.of(
            AuthenticationDto.builder().login("user").password(null).build(), Set.of("password")));
  }

  @BeforeEach
  public void setUp() {
    try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
      validator = factory.getValidator();
    }
  }

  @ParameterizedTest
  @MethodSource("provide_InvalidAuthenticationDto")
  public void validate_whenAuthenticationDtoIsInvalid_thenViolationsIsNotEmpty(
      AuthenticationDto authenticationDto, Set<String> expectedErrorFields) {
    // Do
    var violations = validator.validate(authenticationDto);
    var actualErrorFields =
        violations.stream()
            .map(constraintViolation -> constraintViolation.getPropertyPath().toString())
            .collect(Collectors.toSet());

    // Check
    assertThat(violations.isEmpty()).isFalse();
    assertThat(actualErrorFields).containsExactlyInAnyOrderElementsOf(expectedErrorFields);
  }
}
