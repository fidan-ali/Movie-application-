package com.example.movies.controller;

import com.example.movies.client.model.TmdbMovieDetails;
import com.example.movies.dto.GenreListResponseDto;
import com.example.movies.dto.MovieListResponseDto;
import com.example.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/movies")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/{movieId}")
    public TmdbMovieDetails getMovieDetails(@PathVariable Long movieId) {
        return movieService.getMovieDetails(movieId);
    }

    @GetMapping("/popular")
    public MovieListResponseDto getPopularMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getPopularMovies(page);
    }

    @GetMapping("/top-rated")
    public MovieListResponseDto getTopRatedMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getTopRatedMovies(page);
    }

    @GetMapping("/upcoming")
    public MovieListResponseDto getUpcomingMovies(@RequestParam(defaultValue = "1") int page) {
        return movieService.getUpcomingMovies(page);
    }

    @GetMapping("/genres")
    public GenreListResponseDto getGenres() {
        return movieService.getGenres();
    }
}