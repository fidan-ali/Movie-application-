package com.example.movies.exception;

public class MovieNotFoundException extends LocalizedException {
    public MovieNotFoundException(Long tmdbId) {
        super("error.movie.not-found", tmdbId);
    }
}