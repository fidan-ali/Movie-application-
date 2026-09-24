package com.example.movies.client.model;

import java.util.List;

public record TmdbMovie(
        Long id,
        String title,
        String posterPath,
        String releaseDate,
        Double voteAverage,
        List<Integer> genreIds
) {}