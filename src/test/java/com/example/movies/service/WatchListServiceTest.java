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
import com.example.movies.mapper.WatchListMapper;
import com.example.movies.service.WatchListService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.WATCH_LIST_NAME;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WatchListServiceTest {

    @Mock
    private WatchListRepository watchListRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WatchListMapper mapper;

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

        WatchListResponseDto responseDto =
                new WatchListResponseDto(ID, ID, WATCH_LIST_NAME, null);

        when(userRepository.findById(ID))
                .thenReturn(Optional.of(user));

        when(watchListRepository.existsByUserEntityIdAndName(ID, WATCH_LIST_NAME))
                .thenReturn(false);

        when(mapper.toEntity(request))
                .thenReturn(watchList);

        when(watchListRepository.save(watchList))
                .thenReturn(watchList);

        when(mapper.toResponse(watchList))
                .thenReturn(responseDto);

        WatchListResponseDto result =
                service.createWatchList(ID, request);

        assertEquals(ID, result.id());
        assertEquals(WATCH_LIST_NAME, result.name());
        assertEquals(user, watchList.getUserEntity());

        verify(userRepository).findById(ID);
        verify(watchListRepository)
                .existsByUserEntityIdAndName(ID, WATCH_LIST_NAME);
        verify(mapper).toEntity(request);
        verify(watchListRepository).save(watchList);
        verify(mapper).toResponse(watchList);
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
        verifyNoInteractions(watchListRepository, mapper);
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
        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);

        List<WatchListEntity> watchLists = List.of(watchList);

        WatchlistListResponseDto responseDto =
                new WatchlistListResponseDto(List.of());

        when(watchListRepository.findAllByUserEntityId(ID))
                .thenReturn(watchLists);

        when(mapper.toListDto(watchLists))
                .thenReturn(responseDto);

        WatchlistListResponseDto result =
                service.getUserWatchLists(ID);

        assertEquals(responseDto, result);
    }

    @Test
    void shouldDeleteWatchList() {
        WatchListEntity watchList = new WatchListEntity();
        watchList.setId(ID);

        when(watchListRepository.findById(ID))
                .thenReturn(Optional.of(watchList));

        service.deleteWatchList(watchList.getId());

        verify(watchListRepository).delete(watchList);
    }
}
