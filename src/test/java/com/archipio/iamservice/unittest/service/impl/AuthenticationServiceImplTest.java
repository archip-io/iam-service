package com.archipio.iamservice.unittest.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.quality.Strictness.LENIENT;

import com.archipio.iamservice.client.UserServiceClient;
import com.archipio.iamservice.dto.AuthenticationDto;
import com.archipio.iamservice.dto.CredentialsDto;
import com.archipio.iamservice.dto.JwtTokensDto;
import com.archipio.iamservice.exception.BannedUserException;
import com.archipio.iamservice.exception.CredentialsNotFoundException;
import com.archipio.iamservice.exception.InvalidOrExpiredConfirmationTokenException;
import com.archipio.iamservice.service.JwtService;
import com.archipio.iamservice.service.impl.AuthenticationServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = LENIENT)
class AuthenticationServiceImplTest {

  @Mock JwtService jwtService;

  @Mock UserServiceClient userServiceClient;
  @InjectMocks AuthenticationServiceImpl authenticationService;

  @Test
  public void
      authenticate_whenPasswordIsCorrectAndCredentialsExistsAndUserIsNotBanned_thenReturnNewTokens() {
    // Prepare
    final var login = "user";
    final var username = "user";
    final var email = "user@mail.ru";
    final var isEnabled = true;
    final var authorities = List.of("Authority_1", "Authority_2", "Authority_3");
    final var password = "Password_10";
    final var accessToken = "accessToken";
    final var refreshToken = "refreshToken";
    final var authenticationDto =
        AuthenticationDto.builder().login(login).password(password).build();
    final var credentialsDto =
        CredentialsDto.builder()
            .username(username)
            .email(email)
            .authorities(authorities)
            .isEnabled(isEnabled)
            .build();
    final var jwtTokensDto =
        JwtTokensDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();

    doNothing().when(userServiceClient).validatePassword(login, password);
    when(userServiceClient.findCredentialsByLogin(login)).thenReturn(credentialsDto);
    when(jwtService.createTokens(credentialsDto)).thenReturn(jwtTokensDto);

    // Do
    var actualJwtTokenDto = authenticationService.authenticate(authenticationDto);

    // Check
    verify(userServiceClient, times(1)).validatePassword(login, password);
    verify(userServiceClient, times(1)).findCredentialsByLogin(login);
    verify(jwtService, times(1)).createTokens(credentialsDto);

    assertThat(actualJwtTokenDto).isEqualTo(jwtTokensDto);
  }

  @Test
  public void authenticate_whenPasswordIsIncorrect_thenThrownHttpClientErrorException() {
    // Prepare
    final var login = "user";
    final var password = "Password_10";
    final var authenticationDto =
        AuthenticationDto.builder().login(login).password(password).build();

    doThrow(HttpClientErrorException.class)
        .when(userServiceClient)
        .validatePassword(login, password);

    // Do
    assertThatExceptionOfType(HttpClientErrorException.class)
        .isThrownBy(() -> authenticationService.authenticate(authenticationDto));

    // Check
    verify(userServiceClient, times(1)).validatePassword(login, password);
  }

  @Test
  public void
      authenticate_whenPasswordIsCorrectAndCredentialsNotFound_thenThrownCredentialsNotFoundException() {
    // Prepare
    final var login = "user";
    final var password = "Password_10";
    final var authenticationDto =
        AuthenticationDto.builder().login(login).password(password).build();

    doNothing().when(userServiceClient).validatePassword(login, password);
    when(userServiceClient.findCredentialsByLogin(login)).thenReturn(null);

    // Do
    assertThatExceptionOfType(CredentialsNotFoundException.class)
        .isThrownBy(() -> authenticationService.authenticate(authenticationDto));

    // Check
    verify(userServiceClient, times(1)).validatePassword(login, password);
    verify(userServiceClient, times(1)).findCredentialsByLogin(login);
  }

  @Test
  public void
      authenticate_whenPasswordIsCorrectAndCredentialsExistsAndUserIsBanned_thenThrownBannedUserException() {
    // Prepare
    final var login = "user";
    final var username = "user";
    final var email = "user@mail.ru";
    final var isEnabled = false;
    final var authorities = List.of("Authority_1", "Authority_2", "Authority_3");
    final var password = "Password_10";
    final var authenticationDto =
        AuthenticationDto.builder().login(login).password(password).build();
    final var credentialsDto =
        CredentialsDto.builder()
            .username(username)
            .email(email)
            .authorities(authorities)
            .isEnabled(isEnabled)
            .build();

    doNothing().when(userServiceClient).validatePassword(login, password);
    when(userServiceClient.findCredentialsByLogin(login)).thenReturn(credentialsDto);

    // Do
    assertThatExceptionOfType(BannedUserException.class)
        .isThrownBy(() -> authenticationService.authenticate(authenticationDto));

    // Check
    verify(userServiceClient, times(1)).validatePassword(login, password);
    verify(userServiceClient, times(1)).findCredentialsByLogin(login);
  }

  @Test
  public void
      refresh_whenTokenIsCorrectAndCredentialsExistsAndUserIsNotBanned_thenReturnNewJwtTokens() {
    // Prepare
    final var token = "oldRefreshToken";
    final var username = "user";
    final var email = "user@mail.ru";
    final var isEnabled = true;
    final var authorities = List.of("Authority_1", "Authority_2", "Authority_3");
    final var accessToken = "accessToken";
    final var refreshToken = "newRefreshToken";
    final var credentialsDto =
        CredentialsDto.builder()
            .username(username)
            .email(email)
            .authorities(authorities)
            .isEnabled(isEnabled)
            .build();
    final var jwtTokensDto =
        JwtTokensDto.builder().accessToken(accessToken).refreshToken(refreshToken).build();

    when(jwtService.validate(token)).thenReturn(true);
    when(jwtService.extractUsername(token)).thenReturn(username);
    when(jwtService.extractEmail(token)).thenReturn(email);
    when(userServiceClient.findCredentialsByUsernameAndEmail(username, email))
        .thenReturn(credentialsDto);
    when(jwtService.createTokens(credentialsDto)).thenReturn(jwtTokensDto);

    // Do
    var actualJwtTokensDto = authenticationService.refresh(token);

    // Check
    verify(jwtService, times(1)).validate(token);
    verify(jwtService, times(1)).extractUsername(token);
    verify(jwtService, times(1)).extractEmail(token);
    verify(userServiceClient, times(1)).findCredentialsByUsernameAndEmail(username, email);
    verify(jwtService, times(1)).createTokens(credentialsDto);

    assertThat(actualJwtTokensDto).isEqualTo(jwtTokensDto);
  }

  @Test
  public void refresh_whenTokenIsIncorrect_thenThrownInvalidOrExpiredConfirmationTokenException() {
    // Prepare
    final var token = "oldRefreshToken";
    when(jwtService.validate(token)).thenReturn(false);

    // Do
    assertThatExceptionOfType(InvalidOrExpiredConfirmationTokenException.class)
        .isThrownBy(() -> authenticationService.refresh(token));

    // Check
    verify(jwtService, times(1)).validate(token);
  }

  @Test
  public void
      refresh_whenTokenIsCorrectAndCredentialsNotFound_thenThrownCredentialsNotFoundException() {
    // Prepare
    final var token = "oldRefreshToken";
    final var username = "user";
    final var email = "user@mail.ru";

    when(jwtService.validate(token)).thenReturn(true);
    when(jwtService.extractUsername(token)).thenReturn(username);
    when(jwtService.extractEmail(token)).thenReturn(email);
    when(userServiceClient.findCredentialsByUsernameAndEmail(username, email)).thenReturn(null);

    // Do
    assertThatExceptionOfType(CredentialsNotFoundException.class)
        .isThrownBy(() -> authenticationService.refresh(token));

    // Check
    verify(jwtService, times(1)).validate(token);
    verify(jwtService, times(1)).extractUsername(token);
    verify(jwtService, times(1)).extractEmail(token);
    verify(userServiceClient, times(1)).findCredentialsByUsernameAndEmail(username, email);
  }

  @Test
  public void
      refresh_whenTokenIsCorrectAndCredentialsExistsAndUserIsBanned_thenReturnNewJwtTokens() {
    // Prepare
    final var token = "oldRefreshToken";
    final var username = "user";
    final var email = "user@mail.ru";
    final var isEnabled = false;
    final var authorities = List.of("Authority_1", "Authority_2", "Authority_3");
    final var credentialsDto =
        CredentialsDto.builder()
            .username(username)
            .email(email)
            .authorities(authorities)
            .isEnabled(isEnabled)
            .build();

    when(jwtService.validate(token)).thenReturn(true);
    when(jwtService.extractUsername(token)).thenReturn(username);
    when(jwtService.extractEmail(token)).thenReturn(email);
    when(userServiceClient.findCredentialsByUsernameAndEmail(username, email))
        .thenReturn(credentialsDto);

    // Do
    assertThatExceptionOfType(BannedUserException.class)
        .isThrownBy(() -> authenticationService.refresh(token));

    // Check
    verify(jwtService, times(1)).validate(token);
    verify(jwtService, times(1)).extractUsername(token);
    verify(jwtService, times(1)).extractEmail(token);
    verify(userServiceClient, times(1)).findCredentialsByUsernameAndEmail(username, email);
  }
}
