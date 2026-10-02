package com.example.movies.controller;

import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import com.example.movies.security.AuthenticatedUser;
import com.example.movies.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @GetMapping
  public UserResponseDto getUser(@AuthenticationPrincipal AuthenticatedUser principal) {
    return userService.getUserById(principal.getId());
  }

  @PutMapping
  public UserResponseDto updateUser(
      @AuthenticationPrincipal AuthenticatedUser principal,
      @Valid @RequestBody UserRequestDto request) {
    return userService.updateUser(principal.getId(), request);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deleteUser(@AuthenticationPrincipal AuthenticatedUser principal) {
    userService.deleteUserById(principal.getId());
  }
}