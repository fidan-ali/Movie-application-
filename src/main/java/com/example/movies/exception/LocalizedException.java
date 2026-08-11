package com.example.movies.exception;

import lombok.Getter;

@Getter
public abstract class LocalizedException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String messageCode;
    private final Object[] args;

    protected LocalizedException(ErrorCode errorCode, String messageCode, Object... args) {
        super(messageCode);
        this.errorCode = errorCode;
        this.messageCode = messageCode;
        this.args = args;
    }
}