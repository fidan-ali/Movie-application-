package com.example.movies.exception;

public class DuplicateResourceException extends LocalizedException {

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super("error.duplicate-resource", resourceName, fieldName, fieldValue);
    }
}