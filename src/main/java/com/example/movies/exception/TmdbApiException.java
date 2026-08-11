package com.example.movies.exception;

import lombok.Getter;

@Getter
public class TmdbApiException extends LocalizedException {

    private final int tmdbStatusCode;

    public TmdbApiException(int tmdbStatusCode, Throwable cause) {
        super(ErrorCode.TMDB_UNAVAILABLE, "error.tmdb.unavailable");
        this.tmdbStatusCode = tmdbStatusCode;
        initCause(cause);
    }
}