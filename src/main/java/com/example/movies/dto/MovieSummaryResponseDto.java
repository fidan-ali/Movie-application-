package com.example.movies.dto;

import java.util.List;

public record MovieSummaryResponseDto(
        Long id,
        String title,
        String posterPath,
        String releaseDate,
        Double voteAverage,
        List<String> genres
) {
}