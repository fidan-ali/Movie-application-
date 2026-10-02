package com.example.movies.dto;

import java.util.List;

public record GenreListResponseDto(
        List<GenreResponseDto> genres
) {
}