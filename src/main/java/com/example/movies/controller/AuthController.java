package com.example.movies.controller;

import com.example.movies.dto.AuthResponseDto;
import com.example.movies.dto.LoginRequestDto;
import com.example.movies.dto.RefreshRequestDto;
import com.example.movies.dto.RegisterRequestDto;
import com.example.movies.dto.UserResponseDto;
import com.example.movies.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public UserResponseDto register(@Valid @RequestBody RegisterRequestDto request) {
    return authService.register(request);
  }

  @PostMapping("/login")
  public AuthResponseDto login(@Valid @RequestBody LoginRequestDto request) {
    return authService.login(request);
  }

  @PostMapping("/refresh")
  public AuthResponseDto refresh(@Valid @RequestBody RefreshRequestDto request) {
    return authService.refresh(request);
  }

  @PostMapping("/logout")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void logout(@Valid @RequestBody RefreshRequestDto request) {
    authService.logout(request);
  }
}
