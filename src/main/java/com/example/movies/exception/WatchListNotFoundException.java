package com.example.movies.exception;

public class WatchListNotFoundException extends LocalizedException {
    public WatchListNotFoundException(Long id) {
        super(ErrorCode.WATCHLIST_NOT_FOUND, "error.watchlist.not-found", id);
    }
}