package com.example.movies.service;

import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dao.entity.WatchListEntity;
import com.example.movies.dao.repository.UserRepository;
import com.example.movies.dao.repository.WatchListRepository;
import com.example.movies.dto.WatchListRequestDto;
import com.example.movies.dto.WatchListResponseDto;
import com.example.movies.dto.WatchlistListResponseDto;
import com.example.movies.exception.DuplicateResourceException;
import com.example.movies.exception.UserNotFoundException;
import com.example.movies.exception.WatchListNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.WATCH_LIST_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WatchListServiceTest {

    @Mock
    private WatchListRepository watchListRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private WatchListService service;

    @Test
    void shouldCreateWatchList() {
        UserEntity user = new UserEntity();
        user.setId(ID);

        WatchListRequestDto request = new WatchListRequestDto();
        request.setName(WATCH_LIST_NAME);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setName(WATCH_LIST_NAME);
        watchList.setUserEntity(user);

        when(userRepository.findById(ID))
                .thenReturn(Optional.of(user));

        when(watchListRepository.existsByUserEntityIdAndName(ID, WATCH_LIST_NAME))
                .thenReturn(false);

        when(watchListRepository.save(any(WatchListEntity.class)))
                .thenReturn(watchList);

        WatchListResponseDto result =
                service.createWatchList(ID, request);

        assertEquals(ID, result.id());
        assertEquals(ID, result.userId());
        assertEquals(WATCH_LIST_NAME, result.name());

        ArgumentCaptor<WatchListEntity> captor =
                ArgumentCaptor.forClass(WatchListEntity.class);
        verify(watchListRepository).save(captor.capture());
        assertEquals(user, captor.getValue().getUserEntity());
        assertEquals(WATCH_LIST_NAME, captor.getValue().getName());

        verify(userRepository).findById(ID);
        verify(watchListRepository)
                .existsByUserEntityIdAndName(ID, WATCH_LIST_NAME);
    }

    @Test
    void shouldThrowUserNotFoundExceptionWhenUserDoesNotExist() {
        WatchListRequestDto request = new WatchListRequestDto();
        request.setName(WATCH_LIST_NAME);

        when(userRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> service.createWatchList(ID, request)
        );

        verify(userRepository).findById(ID);
        verifyNoInteractions(watchListRepository);
    }

    @Test
    void shouldThrowDuplicateResourceExceptionWhenWatchListAlreadyExists() {
        UserEntity user = new UserEntity();
        user.setId(ID);

        WatchListRequestDto request = new WatchListRequestDto();
        request.setName(WATCH_LIST_NAME);

        when(userRepository.findById(ID))
                .thenReturn(Optional.of(user));

        when(watchListRepository.existsByUserEntityIdAndName(ID, WATCH_LIST_NAME))
                .thenReturn(true);

        assertThrows(
                DuplicateResourceException.class,
                () -> service.createWatchList(ID, request)
        );
    }

    @Test
    void shouldReturnUserWatchLists() {
        UserEntity user = new UserEntity();
        user.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setName(WATCH_LIST_NAME);
        watchList.setUserEntity(user);

        List<WatchListEntity> watchLists = List.of(watchList);

        when(watchListRepository.findAllByUserEntityId(ID))
                .thenReturn(watchLists);

        WatchlistListResponseDto result =
                service.getUserWatchLists(ID);

        assertEquals(1, result.watchlists().size());
        assertEquals(ID, result.watchlists().get(0).id());
        assertEquals(ID, result.watchlists().get(0).userId());
        assertEquals(WATCH_LIST_NAME, result.watchlists().get(0).name());
    }

    @Test
    void shouldDeleteWatchList() {
        UserEntity user = new UserEntity();
        user.setId(ID);

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(user);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        service.deleteWatchList(watchList.getId(), ID);

        verify(watchListRepository).delete(watchList);
    }
    @Test
    void shouldThrowWatchlistNotFoundExceptionWhenWatchlistDoesNotExist() {
        when(watchListRepository.findById(ID))
                .thenReturn(Optional.empty());

        assertThrows(
                WatchListNotFoundException.class,
                () -> service.deleteWatchList(ID, ID)
        );

        verify(watchListRepository, never()).delete(any());
    }

    @Test
    void shouldThrowWatchlistNotFoundExceptionWhenUserDoesNotOwnWatchlist() {
        UserEntity owner = new UserEntity();
        owner.setId(ID);

        Long otherUserId = 2L;

        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);
        watchList.setUserEntity(owner);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        assertThrows(
                WatchListNotFoundException.class,
                () -> service.deleteWatchList(ID, otherUserId)
        );

        verify(watchListRepository, never()).delete(any());
    }
}
