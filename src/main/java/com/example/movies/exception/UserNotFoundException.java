package com.example.movies.exception;

public class UserNotFoundException extends LocalizedException {
    public UserNotFoundException(Long id) {
        super(ErrorCode.USER_NOT_FOUND, "error.user.not-found", id);
    }
}