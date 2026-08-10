package com.example.movies.dto;

import java.time.LocalDateTime;
import java.util.List;

public record WatchListItemResponseDto(
        Long id,
        Long watchlistId,
        Long tmdbMovieId,
        String title,
        List<String> genres,
        Double voteAverage,
        LocalDateTime addedAt
) {
}