package com.example.movies.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshRequestDto(
    @NotBlank(message = "{validation.refresh-token.required}")
    String refreshToken
) {
}