package com.example.movies.controller;

import com.example.movies.client.TmdbClientService;
import com.example.movies.client.model.TmdbMovieDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final TmdbClientService tmdbClientService;

    @GetMapping("/{movieId}")
    public TmdbMovieDetails getMovieDetails(@PathVariable Long movieId) {
        return tmdbClientService.fetchMovieDetails(movieId);
    }


}