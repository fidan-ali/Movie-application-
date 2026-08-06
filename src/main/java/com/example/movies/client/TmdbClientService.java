package com.example.movies.client;

import com.example.movies.client.config.TmdbProperties;
import com.example.movies.client.model.TmdbGenreResponse;
import com.example.movies.client.model.TmdbMovieDetails;
import com.example.movies.client.model.TmdbMovieResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TmdbClientService {
    private final TmdbFeignClient tmdbFeignClient;
    private final TmdbProperties tmdbProperties;

    public TmdbMovieResponse fetchPopularMovies(int page) {
        return tmdbFeignClient.getPopularMovies(page, tmdbProperties.getKey());
    }

    public TmdbMovieResponse fetchTopRatedMovies(int page) {
        return tmdbFeignClient.getTopRatedMovies(page, tmdbProperties.getKey());

    }

    public TmdbMovieResponse fetchUpcomingMovies(int page) {
        return tmdbFeignClient.getUpcomingMovies(page, tmdbProperties.getKey());
    }

    public TmdbMovieDetails fetchMovieDetails(Long tmdbMovieId) {
        return tmdbFeignClient.getMovieDetails(tmdbMovieId, tmdbProperties.getKey());

    }

    public TmdbGenreResponse fetchGenres() {
        return tmdbFeignClient.getGenres(tmdbProperties.getKey());
    }

}
