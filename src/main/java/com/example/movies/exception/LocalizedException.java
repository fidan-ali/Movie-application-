package com.example.movies.exception;

import lombok.Getter;

@Getter
public abstract class LocalizedException extends RuntimeException {
    private final String messageCode;
    private final Object[] args;

    protected LocalizedException(String messageCode, Object... args) {
        super(messageCode);
        this.messageCode = messageCode;
        this.args = args;
    }
}