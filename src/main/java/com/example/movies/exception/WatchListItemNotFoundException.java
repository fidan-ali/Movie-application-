package com.example.movies.exception;

public class WatchListItemNotFoundException extends LocalizedException {
    public WatchListItemNotFoundException(Long id) {
        super(ErrorCode.WATCHLISTITEM_NOT_FOUND, "error.watchlistitem.not-found", id);
    }
}