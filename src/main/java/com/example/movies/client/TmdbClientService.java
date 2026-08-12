package com.example.movies.client;

import com.example.movies.client.config.TmdbCacheProperties;
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

import static com.example.movies.client.TmdbApiConstants.GENRES_CACHE;
import static com.example.movies.client.TmdbApiConstants.MOVIE_DETAILS_CACHE;
import static com.example.movies.client.TmdbApiConstants.POPULAR_MOVIES_CACHE;

@Service
@RequiredArgsConstructor
public class TmdbClientService {

    private final TmdbFeignClient tmdbFeignClient;
    private final TmdbProperties tmdbProperties;
    private final TmdbCacheProperties tmdbCacheProperties;
    private final RedissonClient redissonClient;

    public TmdbMovieResponse fetchPopularMovies(int page) {
        String key = POPULAR_MOVIES_CACHE + "popular_" + page;
        return fetchWithCache(key, popularMoviesTtl(),
                () -> tmdbFeignClient.getPopularMovies(page, tmdbProperties.getKey()));
    }

    public TmdbMovieResponse fetchTopRatedMovies(int page) {
        String key = POPULAR_MOVIES_CACHE + "topRated_" + page;
        return fetchWithCache(key, popularMoviesTtl(),
                () -> tmdbFeignClient.getTopRatedMovies(page, tmdbProperties.getKey()));
    }

    public TmdbMovieResponse fetchUpcomingMovies(int page) {
        String key = POPULAR_MOVIES_CACHE + "upcoming_" + page;
        return fetchWithCache(key, popularMoviesTtl(),
                () -> tmdbFeignClient.getUpcomingMovies(page, tmdbProperties.getKey()));
    }

    public TmdbMovieDetails fetchMovieDetails(Long tmdbMovieId) {
        String key = MOVIE_DETAILS_CACHE + tmdbMovieId;
        return fetchWithCache(key, movieDetailsTtl(),
                () -> tmdbFeignClient.getMovieDetails(tmdbMovieId, tmdbProperties.getKey()));
    }

    public TmdbGenreResponse fetchGenres() {
        return fetchWithCache(GENRES_CACHE, genresTtl(),
                () -> tmdbFeignClient.getGenres(tmdbProperties.getKey()));
    }

    private Duration popularMoviesTtl() {
        return Duration.ofMinutes(tmdbCacheProperties.getPopularMoviesTtlMinutes());
    }

    private Duration movieDetailsTtl() {
        return Duration.ofMinutes(tmdbCacheProperties.getMovieDetailsTtlMinutes());
    }

    private Duration genresTtl() {
        return Duration.ofMinutes(tmdbCacheProperties.getGenresTtlMinutes());
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