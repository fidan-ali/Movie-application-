package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListItemEntity;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_GENRE;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_TITLE;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_VOTE_AVERAGE;
import static com.example.movies.constant.MovieApiTestConstants.TMDB_MOVIE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WatchListItemMapperTest {

    private final WatchListItemMapper mapper =
            Mappers.getMapper(WatchListItemMapper.class);

    @Test
    void shouldMapRequestDtoToEntity() {

        WatchListItemRequestDto request =
                new WatchListItemRequestDto();

        request.setTmdbMovieId(TMDB_MOVIE_ID);

        WatchListItemEntity result =
                mapper.toEntity(request);

        assertEquals(
                TMDB_MOVIE_ID,
                result.getTmdbMovieId()
        );
    }

    @Test
    void shouldMapEntityToResponseDto() {

        WatchListItemEntity item =
                new WatchListItemEntity();

        item.setId(ID);
        item.setTmdbMovieId(TMDB_MOVIE_ID);
        item.setTitle(MOVIE_TITLE);
        item.setGenres(List.of(MOVIE_GENRE));
        item.setVoteAverage(MOVIE_VOTE_AVERAGE);

        WatchListItemResponseDto result =
                mapper.toResponse(item);

        assertEquals(
                ID,
                result.id()
        );

        assertEquals(
                TMDB_MOVIE_ID,
                result.tmdbMovieId()
        );

        assertEquals(
                MOVIE_TITLE,
                result.title()
        );

        assertEquals(
                List.of(MOVIE_GENRE),
                result.genres()
        );

        assertEquals(
                MOVIE_VOTE_AVERAGE,
                result.voteAverage()
        );
    }
}