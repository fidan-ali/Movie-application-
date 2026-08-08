package com.example.movies.exception;

public class WatchlistNotFoundException extends LocalizedException {
    public WatchlistNotFoundException(Long id) {
        super("error.watchlist.not-found", id);
    }
}