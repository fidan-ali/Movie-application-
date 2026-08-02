package com.example.movies.dto;

import java.util.List;

public record TmdbMovie(
        Long id,
        String title,
        String poster_path,
        String release_date,
        Double vote_average,
        List<Integer> genre_ids
) {}
