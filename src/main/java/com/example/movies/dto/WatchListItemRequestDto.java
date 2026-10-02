package com.example.movies.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchListItemRequestDto {

    @NotNull(message = "{validation.tmdbmovieid.required}")
    @Positive(message = "{validation.tmdbmovieid.positive}")
    private Long tmdbMovieId;
}