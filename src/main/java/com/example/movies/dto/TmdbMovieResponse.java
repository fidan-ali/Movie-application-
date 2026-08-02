package com.example.movies.dto;

import java.util.List;

public record TmdbMovieResponse(
        Integer page,
        List<TmdbMovie> results,
        Integer total_pages,
        Integer total_results
) {}
