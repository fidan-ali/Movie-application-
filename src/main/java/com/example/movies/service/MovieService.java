package com.example.movies.service;

import com.example.movies.client.TmdbClientService;
import com.example.movies.client.model.TmdbGenre;
import com.example.movies.client.model.TmdbGenreResponse;
import com.example.movies.client.model.TmdbMovie;
import com.example.movies.client.model.TmdbMovieDetails;
import com.example.movies.client.model.TmdbMovieResponse;
import com.example.movies.dto.GenreListResponseDto;
import com.example.movies.dto.GenreResponseDto;
import com.example.movies.dto.MovieListResponseDto;
import com.example.movies.dto.MovieSummaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.movies.constant.Constant.UNKNOWN_GENRE;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final TmdbClientService tmdbClientService;

    public MovieListResponseDto getPopularMovies(int page) {
        return toMovieListResponse(tmdbClientService.fetchPopularMovies(page));
    }

    public MovieListResponseDto getTopRatedMovies(int page) {
        return toMovieListResponse(tmdbClientService.fetchTopRatedMovies(page));
    }

    public MovieListResponseDto getUpcomingMovies(int page) {
        return toMovieListResponse(tmdbClientService.fetchUpcomingMovies(page));
    }

    public TmdbMovieDetails getMovieDetails(Long movieId) {
        return tmdbClientService.fetchMovieDetails(movieId);
    }

    public GenreListResponseDto getGenres() {
        return toGenreListResponse(tmdbClientService.fetchGenres());
    }

    private MovieListResponseDto toMovieListResponse(TmdbMovieResponse response) {
        Map<Integer, String> genreNamesById = genreIdToNameMap();

        List<MovieSummaryResponseDto> movies = response.results().stream()
                .map(movie -> toMovieSummary(movie, genreNamesById))
                .toList();

        return new MovieListResponseDto(response.page(), response.totalPages(), response.totalResults(), movies);
    }

    private MovieSummaryResponseDto toMovieSummary(TmdbMovie movie, Map<Integer, String> genreNamesById) {
        List<String> genreNames = movie.genreIds().stream()
                .map(id -> genreNamesById.getOrDefault(id, UNKNOWN_GENRE))
                .toList();

        return new MovieSummaryResponseDto(
                movie.id(),
                movie.title(),
                movie.posterPath(),
                movie.releaseDate(),
                movie.voteAverage(),
                genreNames
        );
    }

    private Map<Integer, String> genreIdToNameMap() {
        return tmdbClientService.fetchGenres().genres().stream()
                .collect(Collectors.toMap(TmdbGenre::id, TmdbGenre::name));
    }

    private GenreListResponseDto toGenreListResponse(TmdbGenreResponse response) {
        List<GenreResponseDto> genres = response.genres().stream()
                .map(g -> new GenreResponseDto(g.id(), g.name()))
                .toList();

        return new GenreListResponseDto(genres);
    }
}