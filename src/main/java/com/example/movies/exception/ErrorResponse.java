package com.example.movies.exception;

public record ErrorResponse(
        ErrorDetail error
) {
}