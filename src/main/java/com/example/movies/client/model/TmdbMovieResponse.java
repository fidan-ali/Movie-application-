package com.example.movies.client.model;

import java.util.List;

public record TmdbMovieResponse(
        Integer page,
        Integer totalPages,
        Integer totalResults,
        List<TmdbMovie> results
) {}