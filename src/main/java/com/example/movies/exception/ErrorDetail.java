package com.example.movies.exception;

public record ErrorDetail(
        String code,
        String message
) {
}