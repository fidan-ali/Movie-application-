package com.example.movies.exception;

public class MovieNotFoundException extends LocalizedException {
    public MovieNotFoundException(Long tmdbId) {
        super(ErrorCode.MOVIE_NOT_FOUND, "error.movie.not-found", tmdbId);
    }
}