// GlobalExceptionHandler.java
package com.example.movies.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return buildError("USER_NOT_FOUND", ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(MovieNotFoundException.class)
    public ErrorResponse handleMovieNotFound(MovieNotFoundException ex) {
        return buildError("MOVIE_NOT_FOUND", ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WatchListNotFoundException.class)
    public ErrorResponse handleWatchlistNotFoundException(WatchListNotFoundException ex) {
        return buildError("WATCHLIST_NOT_FOUND", ex);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WatchListItemNotFoundException.class)
    public ErrorResponse handleWatchListItemNotFound(WatchListItemNotFoundException ex) {
        return buildError("WATCHLISTITEM_NOT_FOUND", ex);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateResourceException.class)
    public ErrorResponse handleDuplicateResource(DuplicateResourceException ex) {
        return buildError("DUPLICATE_RESOURCE", ex);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(TmdbApiException.class)
    public ErrorResponse handleTmdbApiException(TmdbApiException ex) {
        log.error("TMDB API call failed (status {}): {}", ex.getTmdbStatusCode(), ex.getMessage(), ex);
        return buildError("TMDB_UNAVAILABLE", ex);
    }

    private ErrorResponse buildError(String code, LocalizedException ex) {
        String message = messageSource.getMessage(ex.getMessageCode(), ex.getArgs(), LocaleContextHolder.getLocale());
        return new ErrorResponse(new ErrorDetail(code, message));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleValidationException(MethodArgumentNotValidException ex) {
        String combinedMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return new ErrorResponse(new ErrorDetail("VALIDATION_FAILED", combinedMessage));
    }
}