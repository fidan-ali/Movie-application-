package com.example.movies.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return buildError("USER_NOT_FOUND", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MovieNotFoundException.class)
    public ErrorResponse handleMovieNotFound(MovieNotFoundException ex) {
        return buildError("MOVIE_NOT_FOUND", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WatchlistNotFoundException.class)
    public ErrorResponse handleWatchlistNotFoundException(WatchlistNotFoundException ex) {
        return buildError("WATCHLIST_NOT_FOUND", ex.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(TmdbApiException.class)
    public ErrorResponse handleTmdbApiException(TmdbApiException ex) {
        log.error("TMDB API call failed: {}", ex.getMessage(), ex);
        return buildError("TMDB_UNAVAILABLE", "Movie data provider is currently unavailable. Please try again later.");
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateResourceException.class)
    public ErrorResponse handleDuplicateResource(DuplicateResourceException ex) {
        return buildError("DUPLICATE_RESOURCE", ex.getMessage());
    }

    private ErrorResponse buildError(String code, String message) {
        return new ErrorResponse(
                new ErrorDetail(code, message)
        );
    }

}
