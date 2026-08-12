package com.example.movies.client.model;

import java.util.List;

public record TmdbMovieDetails(
        Long id,
        String title,
        String overview,
        String releaseDate,
        Integer runtime,
        List<TmdbGenre> genres,
        Double voteAverage,
        String posterPath,
        String backdropPath
) {}
