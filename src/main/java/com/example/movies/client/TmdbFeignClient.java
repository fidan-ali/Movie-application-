package com.example.movies.client;

import com.example.movies.client.config.TmdbErrorDecoder;
import com.example.movies.client.model.TmdbGenreResponse;
import com.example.movies.client.model.TmdbMovieDetails;
import com.example.movies.client.model.TmdbMovieResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.example.movies.client.TmdbApiConstants.API_KEY;

@FeignClient(
        name = "tmdb-client",
        url = "${tmdb.api.base-url}",
        configuration = TmdbErrorDecoder.class
)
public interface TmdbFeignClient {

    @GetMapping("/3/movie/popular")
    TmdbMovieResponse getPopularMovies(
            @RequestParam Integer page,
            @RequestParam(API_KEY) String apiKey
    );

    @GetMapping("/3/movie/top_rated")
    TmdbMovieResponse getTopRatedMovies(
            @RequestParam Integer page,
            @RequestParam(API_KEY) String apiKey
    );

    @GetMapping("/3/movie/upcoming")
    TmdbMovieResponse getUpcomingMovies(
            @RequestParam Integer page,
            @RequestParam(API_KEY) String apiKey
    );

    @GetMapping("/3/movie/{movieId}")
    TmdbMovieDetails getMovieDetails(
            @PathVariable Long movieId,
            @RequestParam(API_KEY) String apiKey
    );

    @GetMapping("/3/genre/movie/list")
    TmdbGenreResponse getGenres(
            @RequestParam(API_KEY) String apiKey
    );
}