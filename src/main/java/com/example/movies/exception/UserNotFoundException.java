package com.example.movies.exception;

public class UserNotFoundException extends LocalizedException {
    public UserNotFoundException(Long id) {
        super("error.user.not-found", id);
    }
}