package com.example.movies.exception;

public class MovieNotFoundException extends RuntimeException{
    public MovieNotFoundException(Long tmdbId) {
        super("Movie not found with TMDB id: " + tmdbId);
    }
}
