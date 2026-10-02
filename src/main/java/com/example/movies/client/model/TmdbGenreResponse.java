package com.example.movies.client.model;

import java.util.List;

public record TmdbGenreResponse(
        List<TmdbGenre> genres
) {}