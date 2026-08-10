package com.example.movies.exception;

public class WatchListNotFoundException extends LocalizedException {
    public WatchListNotFoundException(Long id) {
        super("error.watchlist.not-found", id);
    }
}