package com.example.movies.dto;

public record AuthResponseDto(
    String accessToken,
    String refreshToken,
    String tokenType
) {
}