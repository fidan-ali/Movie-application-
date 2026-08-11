package com.example.movies.controller;

import com.example.movies.client.TmdbClientService;
import com.example.movies.client.model.TmdbMovieDetails;
import com.example.movies.service.RecentlyViewedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final TmdbClientService tmdbClientService;
    private final RecentlyViewedService recentlyViewedService;

    @GetMapping("/{movieId}")
    public TmdbMovieDetails getMovieDetails(
            @PathVariable Long movieId,
            @RequestHeader("userId") Long userId) {

        TmdbMovieDetails details = tmdbClientService.fetchMovieDetails(movieId);
        recentlyViewedService.recordView(userId, movieId);
        return details;
    }

    @GetMapping("/recent")
    public List<TmdbMovieDetails> getRecentlyViewed(
            @RequestHeader("userId") Long userId) {

        return recentlyViewedService.getRecentMovieIds(userId).stream()
                .map(tmdbClientService::fetchMovieDetails)
                .toList();
    }
}