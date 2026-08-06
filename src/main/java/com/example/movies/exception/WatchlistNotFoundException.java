package com.example.movies.exception;

public class WatchlistNotFoundException extends RuntimeException{
    public WatchlistNotFoundException(Long id){
        super("Watchlist not found with id: " + id);
    }
}
