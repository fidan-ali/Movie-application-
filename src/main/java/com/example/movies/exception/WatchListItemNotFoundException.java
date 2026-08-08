package com.example.movies.exception;

public class WatchListItemNotFoundException extends LocalizedException {
    public WatchListItemNotFoundException(Long id) {
        super("error.watchlistitem.not-found", id);
    }
}