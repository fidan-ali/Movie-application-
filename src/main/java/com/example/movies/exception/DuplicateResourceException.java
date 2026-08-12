package com.example.movies.exception;

public class DuplicateResourceException extends LocalizedException {
    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(ErrorCode.DUPLICATE_RESOURCE, "error.duplicate-resource", resourceName, fieldName, fieldValue);
    }
}