package com.example.movies.controller;

import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import com.example.movies.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.movies.constant.Constant.HEADER_X_USER_ID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto request) {
        return userService.createUser(request);
    }

    @GetMapping
    public UserResponseDto getUser(@RequestHeader(HEADER_X_USER_ID) Long userId) {
        return userService.getUserById(userId);
    }

    @PutMapping
    public UserResponseDto updateUser(
            @RequestHeader(HEADER_X_USER_ID) Long userId,
            @Valid @RequestBody UserRequestDto request) {
        return userService.updateUser(userId, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@RequestHeader(HEADER_X_USER_ID) Long userId) {
        userService.deleteUserById(userId);
    }
}