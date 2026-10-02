package com.example.movies.dto;

import java.util.List;

public record WatchlistListResponseDto(
        List<WatchListResponseDto> watchlists
) {
}