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
import com.example.movies.mapper.WatchListMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.movies.constant.Constant.NAME;
import static com.example.movies.constant.Constant.WATCH_LIST;

@Service
@RequiredArgsConstructor
public class WatchListService {
    private final WatchListRepository watchListRepository;
    private final WatchListMapper watchListMapper = WatchListMapper.INSTANCE;
    private final UserRepository userRepository;

    @Transactional
    public WatchListResponseDto createWatchList(Long userId, WatchListRequestDto request) {

        UserEntity user = userRepository.findById(userId).
                orElseThrow(() -> new UserNotFoundException(userId));
        if (watchListRepository.existsByUserEntityIdAndName(userId, request.getName())) {
            throw new DuplicateResourceException(WATCH_LIST, NAME, request.getName());
        }

        WatchListEntity watchList = watchListMapper.toEntity(request);
        watchList.setUserEntity(user);
        WatchListEntity saved = watchListRepository.save(watchList);
        return watchListMapper.toResponse(saved);
    }

    public WatchlistListResponseDto getUserWatchLists(Long userId) {
        List<WatchListEntity> watchLists = watchListRepository.findAllByUserEntityId(userId);
        return watchListMapper.toListDto(watchLists);
    }

    @Transactional
    public void deleteWatchList(Long id, Long userId) {
        WatchListEntity watchList = watchListRepository.findById(id).
                orElseThrow(() -> new WatchListNotFoundException(id));
        if (!watchList.getUserEntity().getId().equals(userId)) {
            throw new WatchListNotFoundException(id);
        }
        watchListRepository.delete(watchList);
    }

}