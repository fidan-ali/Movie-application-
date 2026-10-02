package com.example.movies.service;

import com.example.movies.client.TmdbClientService;
import com.example.movies.client.model.TmdbGenre;
import com.example.movies.client.model.TmdbMovieDetails;
import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dao.entity.WatchListEntity;
import com.example.movies.dao.entity.WatchListItemEntity;
import com.example.movies.dao.repository.WatchListItemRepository;
import com.example.movies.dao.repository.WatchListRepository;
import com.example.movies.dto.WatchListItemPageResponseDto;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import com.example.movies.exception.DuplicateResourceException;
import com.example.movies.exception.WatchListItemNotFoundException;
import com.example.movies.exception.WatchListNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.movies.constant.MovieApiTestConstants.ANOTHER_WATCHLIST_ID;
import static com.example.movies.constant.MovieApiTestConstants.FIRST_PAGE;
import static com.example.movies.constant.MovieApiTestConstants.GENRE_ID;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.ITEM_ID_1;
import static com.example.movies.constant.MovieApiTestConstants.ITEM_ID_2;
import static com.example.movies.constant.MovieApiTestConstants.ITEM_ID_3;
import static com.example.movies.constant.MovieApiTestConstants.LARGE_PAGE_SIZE;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_BACKDROP_PATH;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_GENRE;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_OVERVIEW;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_POSTER_PATH;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_RELEASE_DATE;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_RUNTIME;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_TITLE;
import static com.example.movies.constant.MovieApiTestConstants.MOVIE_VOTE_AVERAGE;
import static com.example.movies.constant.MovieApiTestConstants.PAGE_SIZE;
import static com.example.movies.constant.MovieApiTestConstants.SECOND_PAGE;
import static com.example.movies.constant.MovieApiTestConstants.TMDB_MOVIE_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WatchListItemServiceTest {

    @Mock
    private WatchListItemRepository watchListItemRepository;

    @Mock
    private WatchListRepository watchListRepository;

    @Mock
    private TmdbClientService tmdbClientService;

    @InjectMocks
    private WatchListItemService service;


    @Test
    void shouldAddItemToWatchlist() {

        UserEntity owner = new UserEntity();
        owner.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(owner);

        WatchListItemRequestDto request =
                new WatchListItemRequestDto();
        request.setTmdbMovieId(TMDB_MOVIE_ID);

        TmdbGenre genre = new TmdbGenre(
                GENRE_ID,
                MOVIE_GENRE
        );

        TmdbMovieDetails movieDetails =
                new TmdbMovieDetails(
                        TMDB_MOVIE_ID,
                        MOVIE_TITLE,
                        MOVIE_OVERVIEW,
                        MOVIE_RELEASE_DATE,
                        MOVIE_RUNTIME,
                        List.of(genre),
                        MOVIE_VOTE_AVERAGE,
                        MOVIE_POSTER_PATH,
                        MOVIE_BACKDROP_PATH
                );

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        when(watchListItemRepository
                .existsByWatchlistIdAndTmdbMovieId(
                        ID,
                        TMDB_MOVIE_ID
                ))
                .thenReturn(false);

        when(tmdbClientService.fetchMovieDetails(TMDB_MOVIE_ID))
                .thenReturn(movieDetails);

        when(watchListItemRepository.save(any(WatchListItemEntity.class)))
                .thenAnswer(invocation -> {
                    WatchListItemEntity saved = invocation.getArgument(0);
                    saved.setId(ID);
                    return saved;
                });

        WatchListItemResponseDto result =
                service.addItemToWatchlist(ID, ID, request);

        assertEquals(ID, result.id());
        assertEquals(ID, result.watchlistId());
        assertEquals(TMDB_MOVIE_ID, result.tmdbMovieId());
        assertEquals(MOVIE_TITLE, result.title());
        assertEquals(List.of(MOVIE_GENRE), result.genres());
        assertEquals(MOVIE_VOTE_AVERAGE, result.voteAverage());

        ArgumentCaptor<WatchListItemEntity> captor =
                ArgumentCaptor.forClass(WatchListItemEntity.class);
        verify(watchListItemRepository).save(captor.capture());
        assertEquals(watchList, captor.getValue().getWatchlist());

        verify(watchListRepository).findById(ID);

        verify(watchListItemRepository)
                .existsByWatchlistIdAndTmdbMovieId(
                        ID,
                        TMDB_MOVIE_ID
                );

        verify(tmdbClientService)
                .fetchMovieDetails(TMDB_MOVIE_ID);
    }


    @Test
    void shouldThrowWatchlistNotFoundExceptionWhenWatchlistDoesNotExist() {

        WatchListItemRequestDto request =
                new WatchListItemRequestDto();
        request.setTmdbMovieId(TMDB_MOVIE_ID);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThrows(
                WatchListNotFoundException.class,
                () -> service.addItemToWatchlist(ID, ID, request)
        );

        verify(watchListRepository).findById(ID);

        verifyNoInteractions(
                watchListItemRepository,
                tmdbClientService
        );
    }


    @Test
    void shouldThrowDuplicateResourceExceptionWhenItemAlreadyExists() {

        UserEntity owner = new UserEntity();
        owner.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(owner);

        WatchListItemRequestDto request =
                new WatchListItemRequestDto();
        request.setTmdbMovieId(TMDB_MOVIE_ID);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        when(watchListItemRepository
                .existsByWatchlistIdAndTmdbMovieId(
                        ID,
                        TMDB_MOVIE_ID
                ))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> service.addItemToWatchlist(ID, ID, request)
        );

        verify(watchListRepository).findById(ID);

        verify(watchListItemRepository)
                .existsByWatchlistIdAndTmdbMovieId(
                        ID,
                        TMDB_MOVIE_ID
                );

        verifyNoInteractions(tmdbClientService);

        verify(watchListItemRepository, never())
                .save(any());
    }


    @Test
    void shouldDeleteWatchListItem() {

        UserEntity owner = new UserEntity();
        owner.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(owner);

        WatchListItemEntity item =
                new WatchListItemEntity();
        item.setId(ID);
        item.setWatchlist(watchList);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        when(watchListItemRepository.findById(ID))
                .thenReturn(Optional.of(item));

        service.deleteWatchListItem(ID, ID, ID);

        verify(watchListRepository).findById(ID);
        verify(watchListItemRepository).findById(ID);
        verify(watchListItemRepository).delete(item);
    }


    @Test
    void shouldThrowWatchListItemNotFoundExceptionWhenItemDoesNotExist() {

        UserEntity owner = new UserEntity();
        owner.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(owner);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        when(watchListItemRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThrows(
                WatchListItemNotFoundException.class,
                () -> service.deleteWatchListItem(ID, ID, ID)
        );

        verify(watchListRepository).findById(ID);
        verify(watchListItemRepository).findById(ID);

        verify(watchListItemRepository, never())
                .delete(any());
    }


    @Test
    void shouldThrowWatchListItemNotFoundExceptionWhenItemBelongsToAnotherWatchlist() {

        UserEntity owner = new UserEntity();
        owner.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(owner);

        WatchListEntity anotherWatchList =
                new WatchListEntity();
        anotherWatchList.setId(ANOTHER_WATCHLIST_ID);

        WatchListItemEntity item =
                new WatchListItemEntity();
        item.setId(ID);
        item.setWatchlist(anotherWatchList);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        when(watchListItemRepository.findById(ID))
                .thenReturn(Optional.of(item));

        assertThrows(
                WatchListItemNotFoundException.class,
                () -> service.deleteWatchListItem(ID, ID, ID)
        );

        verify(watchListRepository).findById(ID);
        verify(watchListItemRepository).findById(ID);

        verify(watchListItemRepository, never())
                .delete(any());
    }


    @Test
    void shouldReturnWatchListItemsPage() {

        UserEntity owner = new UserEntity();
        owner.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(owner);

        WatchListItemEntity item1 =
                new WatchListItemEntity();
        item1.setId(ITEM_ID_1);

        WatchListItemEntity item2 =
                new WatchListItemEntity();
        item2.setId(ITEM_ID_2);

        List<WatchListItemEntity> items =
                List.of(item1, item2);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        when(watchListItemRepository.findAllByWatchlistId(ID))
                .thenReturn(items);

        WatchListItemPageResponseDto result =
                service.getWatchListItems(
                        ID,
                        ID,
                        FIRST_PAGE,
                        LARGE_PAGE_SIZE
                );

        assertEquals(
                FIRST_PAGE,
                result.page()
        );

        assertEquals(
                FIRST_PAGE,
                result.totalPages()
        );

        assertEquals(
                ITEM_ID_2,
                result.totalResults()
        );

        assertEquals(2, result.items().size());
        assertEquals(ITEM_ID_1, result.items().get(0).id());
        assertEquals(ITEM_ID_2, result.items().get(1).id());

        verify(watchListRepository)
                .findById(ID);

        verify(watchListItemRepository)
                .findAllByWatchlistId(ID);
    }


    @Test
    void shouldReturnSecondPageOfWatchListItems() {

        UserEntity owner = new UserEntity();
        owner.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(owner);

        WatchListItemEntity item1 =
                new WatchListItemEntity();
        item1.setId(ITEM_ID_1);

        WatchListItemEntity item2 =
                new WatchListItemEntity();
        item2.setId(ITEM_ID_2);

        WatchListItemEntity item3 =
                new WatchListItemEntity();
        item3.setId(ITEM_ID_3);

        List<WatchListItemEntity> items =
                List.of(item1, item2, item3);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        when(watchListItemRepository.findAllByWatchlistId(ID))
                .thenReturn(items);

        WatchListItemPageResponseDto result =
                service.getWatchListItems(
                        ID,
                        ID,
                        SECOND_PAGE,
                        PAGE_SIZE
                );

        assertEquals(
                SECOND_PAGE,
                result.page()
        );

        assertEquals(
                SECOND_PAGE,
                result.totalPages()
        );

        assertEquals(
                ITEM_ID_3,
                result.totalResults()
        );

        assertEquals(1, result.items().size());
        assertEquals(ITEM_ID_3, result.items().get(0).id());

        verify(watchListRepository)
                .findById(ID);

        verify(watchListItemRepository)
                .findAllByWatchlistId(ID);
    }


    @Test
    void shouldThrowWatchListNotFoundExceptionWhenWatchListDoesNotExist() {

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThrows(
                WatchListNotFoundException.class,
                () -> service.getWatchListItems(
                        ID,
                        ID,
                        FIRST_PAGE,
                        LARGE_PAGE_SIZE
                )
        );

        verify(watchListRepository)
                .findById(ID);

        verify(watchListItemRepository, never())
                .findAllByWatchlistId(any());

    }
}