package com.example.movies.dto;

import java.util.List;

public record MovieListResponseDto(
        Integer page,
        Integer totalPages,
        Integer totalResults,
        List<MovieSummaryResponseDto> results
) {
}