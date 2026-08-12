package com.example.movies.dto;

import java.util.List;

public record WatchListItemPageResponseDto(
        int page,
        int totalPages,
        int totalResults,
        List<WatchListItemResponseDto> items
) {
}