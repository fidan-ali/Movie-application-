package com.example.movies.exception;

public class WatchListItemNotFoundException extends RuntimeException{
    public WatchListItemNotFoundException(Long id) {
        super("WatchListItem not found with id: " + id);
    }
}
