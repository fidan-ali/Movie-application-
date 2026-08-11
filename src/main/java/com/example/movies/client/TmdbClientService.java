package com.example.movies.client;

import com.example.movies.client.config.TmdbProperties;
import com.example.movies.client.model.TmdbGenreResponse;
import com.example.movies.client.model.TmdbMovieDetails;
import com.example.movies.client.model.TmdbMovieResponse;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class TmdbClientService {

    private static final String POPULAR_MOVIES_CACHE = "popularMovies::";
    private static final String MOVIE_DETAILS_CACHE = "movieDetails::";
    private static final String GENRES_CACHE = "genres";

    private static final Duration POPULAR_MOVIES_TTL = Duration.ofMinutes(15);
    private static final Duration MOVIE_DETAILS_TTL = Duration.ofHours(6);
    private static final Duration GENRES_TTL = Duration.ofDays(1);

    private final TmdbFeignClient tmdbFeignClient;
    private final TmdbProperties tmdbProperties;
    private final RedissonClient redissonClient;

    public TmdbMovieResponse fetchPopularMovies(int page) {
        String key = POPULAR_MOVIES_CACHE + "popular_" + page;
        return fetchWithCache(key, POPULAR_MOVIES_TTL,
                () -> tmdbFeignClient.getPopularMovies(page, tmdbProperties.getKey()));
    }

    public TmdbMovieResponse fetchTopRatedMovies(int page) {
        String key = POPULAR_MOVIES_CACHE + "topRated_" + page;
        return fetchWithCache(key, POPULAR_MOVIES_TTL,
                () -> tmdbFeignClient.getTopRatedMovies(page, tmdbProperties.getKey()));
    }

    public TmdbMovieResponse fetchUpcomingMovies(int page) {
        String key = POPULAR_MOVIES_CACHE + "upcoming_" + page;
        return fetchWithCache(key, POPULAR_MOVIES_TTL,
                () -> tmdbFeignClient.getUpcomingMovies(page, tmdbProperties.getKey()));
    }

    public TmdbMovieDetails fetchMovieDetails(Long tmdbMovieId) {
        String key = MOVIE_DETAILS_CACHE + tmdbMovieId;
        return fetchWithCache(key, MOVIE_DETAILS_TTL,
                () -> tmdbFeignClient.getMovieDetails(tmdbMovieId, tmdbProperties.getKey()));
    }

    public TmdbGenreResponse fetchGenres() {
        return fetchWithCache(GENRES_CACHE, GENRES_TTL,
                () -> tmdbFeignClient.getGenres(tmdbProperties.getKey()));
    }

    private <T> T fetchWithCache(String key, Duration ttl, Supplier<T> loader) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        T cached = bucket.get();

        if (cached != null) {
            return cached;
        }

        T loaded = loader.get();
        bucket.set(loaded, ttl);
        return loaded;
    }
}