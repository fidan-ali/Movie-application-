package com.example.movies.constant;

import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import com.example.movies.dto.WatchListItemPageResponseDto;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import com.example.movies.dto.WatchListRequestDto;
import com.example.movies.dto.WatchListResponseDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class MovieApiTestConstants {

    public static final String BASE_USER_URL = "/api/v1/users";
    public static final String BASE_WATCHLIST_URL =
            "/api/v1/users/{userId}/watchlists";
    public static final String WATCHLIST_URL = "/api/v1/watchlists/{id}";
    public static final String BASE_WATCHLIST_ITEM_URL =
            "/api/v1/watchlists/{watchlistId}/items";
    public static final String WATCHLIST_ITEM_URL =
            "/api/v1/watchlists/{watchlistId}/items/{itemId}";

    public static final String PAGE_PARAM = "page";
    public static final String PAGE_SIZE_PARAM = "page_size";

    public static final Long ID = 1L;
    public static final Long USER_ID = 1L;
    public static final Long WATCHLIST_ID = 10L;

    public static final String EMAIL = "test@gmail.com";
    public static final String SECOND_EMAIL = "john@gmail.com";

    public static final String FIRST_NAME = "Fidan";
    public static final String LAST_NAME = "Aliyeva";
    public static final String SECOND_FIRST_NAME = "Sheriff";
    public static final String SECOND_LAST_NAME = "Woody";

    public static final LocalDate BIRTH_DATE =
            LocalDate.of(2006, 9, 17);

    public static final LocalDate SECOND_BIRTH_DATE =
            LocalDate.of(2006, 5, 8);

    public static final LocalDate FUTURE_BIRTH_DATE =
            LocalDate.now().plusDays(1);

    public static final LocalDateTime CREATED_AT =
            LocalDateTime.of(2026, 8, 10, 10, 0);

    public static final String BLANK_VALUE = "";
    public static final String INVALID_EMAIL = "invalid-email";
    public static final String TOO_SHORT_NAME = "A";
    public static final String NON_NUMERIC_ID = "abc";

    public static final String WATCH_LIST_NAME = "My Movies";
    public static final String SECOND_WATCH_LIST_NAME = "Favorites";

    public static final Long TMDB_MOVIE_ID = 101L;
    public static final String MOVIE_TITLE = "Adolescence";
    public static final String MOVIE_OVERVIEW = "Movie overview";
    public static final String MOVIE_RELEASE_DATE = "2010-07-16";
    public static final Integer MOVIE_RUNTIME = 148;
    public static final Integer GENRE_ID = 1;
    public static final String MOVIE_GENRE = "Thriller";
    public static final Double MOVIE_VOTE_AVERAGE = 8.8;
    public static final String MOVIE_POSTER_PATH = "/poster.jpg";
    public static final String MOVIE_BACKDROP_PATH = "/backdrop.jpg";

    public static final Long ITEM_ID_1 = 1L;
    public static final Long ITEM_ID_2 = 2L;
    public static final Long ITEM_ID_3 = 3L;

    public static final Long ANOTHER_WATCHLIST_ID = 999L;
    public static final Long ZERO = 0L;

    public static final Integer FIRST_PAGE = 1;
    public static final Integer SECOND_PAGE = 2;
    public static final Integer PAGE_SIZE = 2;
    public static final Integer LARGE_PAGE_SIZE = 10;
    public static final Integer DEFAULT_PAGE_SIZE = 20;

    public static final Integer FIRST_INDEX = 0;
    public static final Integer SECOND_INDEX = 1;
    public static final Integer TWO = 2;

    public static UserRequestDto mockUserRequestDto() {
        UserRequestDto request = new UserRequestDto();
        request.setFirstName(FIRST_NAME);
        request.setLastName(LAST_NAME);
        request.setEmail(EMAIL);
        request.setBirthDate(BIRTH_DATE);
        return request;
    }

    public static UserResponseDto mockUserResponseDto(Long id) {
        return new UserResponseDto(
                id,
                FIRST_NAME,
                LAST_NAME,
                EMAIL,
                BIRTH_DATE,
                CREATED_AT
        );
    }

    public static WatchListRequestDto mockWatchListRequestDto() {
        return new WatchListRequestDto(WATCH_LIST_NAME);
    }

    public static WatchListResponseDto mockWatchListResponseDto() {
        return new WatchListResponseDto(
                WATCHLIST_ID,
                USER_ID,
                WATCH_LIST_NAME,
                CREATED_AT
        );
    }

    public static WatchListItemRequestDto mockWatchListItemRequestDto() {
        return new WatchListItemRequestDto(TMDB_MOVIE_ID);
    }

    public static WatchListItemResponseDto mockWatchListItemResponseDto() {
        return new WatchListItemResponseDto(
                ITEM_ID_1,
                WATCHLIST_ID,
                TMDB_MOVIE_ID,
                MOVIE_TITLE,
                List.of(MOVIE_GENRE),
                MOVIE_VOTE_AVERAGE,
                CREATED_AT
        );
    }

    public static WatchListItemPageResponseDto
    mockWatchListItemPageResponseDto() {
        return new WatchListItemPageResponseDto(
                FIRST_PAGE,
                FIRST_PAGE,
                1,
                List.of(mockWatchListItemResponseDto())
        );
    }
}