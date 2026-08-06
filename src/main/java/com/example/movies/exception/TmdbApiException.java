package com.example.movies.exception;

public class TmdbApiException extends RuntimeException {

    private final int tmdbStatusCode;

    public TmdbApiException(String message, int tmdbStatusCode, Throwable cause) {
        super(message, cause);
        this.tmdbStatusCode = tmdbStatusCode;
    }

    public int getTmdbStatusCode(){
        return tmdbStatusCode;
    }
}
