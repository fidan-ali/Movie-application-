package com.example.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchlistItemResponseDto {
    private Long id;
    private Long watchlistId;
    private Long tmdbMovieId;
    private String title;
    private List<String> genres;
    private Double voteAverage;
    private LocalDateTime addedAt;
}
