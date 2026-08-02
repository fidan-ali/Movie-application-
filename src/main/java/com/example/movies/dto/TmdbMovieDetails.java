package com.example.movies.dto;

import java.util.List;

public record TmdbMovieDetails(
        Long id,
        String title,
        String overview,
        String release_date,
        Integer runtime,
        List<TmdbGenre> genres,
        Double vote_average,
        String poster_path,
        String backdrop_path
) {}
