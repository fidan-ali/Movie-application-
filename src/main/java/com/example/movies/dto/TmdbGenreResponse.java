package com.example.movies.dto;

import java.util.List;

public record TmdbGenreResponse(
        List<TmdbGenre> genres
) {}