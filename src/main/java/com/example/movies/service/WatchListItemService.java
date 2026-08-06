package com.example.movies.service;

import com.example.movies.client.TmdbClientService;
import com.example.movies.client.TmdbFeignClient;
import com.example.movies.client.model.TmdbGenre;
import com.example.movies.client.model.TmdbMovieDetails;
import com.example.movies.dao.entity.WatchListEntity;
import com.example.movies.dao.entity.WatchListItemEntity;
import com.example.movies.dao.repository.WatchListItemRepository;
import com.example.movies.dao.repository.WatchListRepository;
import com.example.movies.dto.WatchListItemPageResponseDto;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import com.example.movies.exception.DuplicateResourceException;
import com.example.movies.exception.WatchListItemNotFoundException;
import com.example.movies.exception.WatchlistNotFoundException;
import com.example.movies.mapper.WatchListItemMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WatchListItemService {

    private final WatchListItemRepository watchListItemRepository;
    private final WatchListRepository watchListRepository;
    private final WatchListItemMapper watchListItemMapper;
    private final TmdbClientService tmdbClientService;
    private final TmdbFeignClient tmdbFeignClient;

    @Value("${tmdb.api.key}")
    private String tmdbApiKey;

    @Transactional
    public WatchListItemResponseDto addItemToWatchlist(Long watchListId, WatchListItemRequestDto request) {
        WatchListEntity watchList = watchListRepository.findById(watchListId).
                orElseThrow(() -> new WatchlistNotFoundException(watchListId));
        if (watchListItemRepository.existsByWatchlistIdAndTmdbMovieId(watchListId, request.getTmdbMovieId())) {
            throw new DuplicateResourceException("WatchListItem", "tmdbMovieId", request.getTmdbMovieId());
        }

        TmdbMovieDetails movieDetails = tmdbFeignClient.getMovieDetails(request.getTmdbMovieId(), tmdbApiKey);

        WatchListItemEntity item = buildItem(request, watchList, movieDetails);


        WatchListItemEntity saved = watchListItemRepository.save(item);
        return watchListItemMapper.toResponse(saved);
    }

    @Transactional
    public void deleteWatchListItem(Long watchListId, Long itemId){
        WatchListItemEntity item = watchListItemRepository.findById(itemId).
                orElseThrow(()-> new WatchListItemNotFoundException(itemId));

        if (!item.getWatchlist().getId().equals(watchListId)) {
            throw new WatchListItemNotFoundException(itemId);
        }

        watchListItemRepository.delete(item);
    }
    public WatchListItemPageResponseDto getWatchListItems(Long watchlistId, int page, int pageSize){
        if(!watchListItemRepository.existsById(watchlistId)) {
            throw new WatchListItemNotFoundException(watchlistId);
        }

        List<WatchListItemEntity> allItems = watchListItemRepository.findAllByWatchlistId(watchlistId);
        return buildPageResponse(allItems, page, pageSize);
    }


    private WatchListItemEntity buildItem(WatchListItemRequestDto request, WatchListEntity watchList,
                                          TmdbMovieDetails movieDetails) {
        WatchListItemEntity item = watchListItemMapper.toEntity(request);
        item.setWatchlist(watchList);
        item.setTitle(movieDetails.title());
        item.setGenres(movieDetails.genres().stream()
                .map(TmdbGenre::name)
                .toList());
        item.setVoteAverage(movieDetails.voteAverage());
        return item;
    }

    private WatchListItemPageResponseDto buildPageResponse(List<WatchListItemEntity> allItems, int page, int pageSize) {
        int totalResults = allItems.size();
        int totalPages = (int) Math.ceil(totalResults / (double) pageSize);
        int fromIndex = Math.min((page - 1) * pageSize, totalResults);
        int toIndex = Math.min(fromIndex + pageSize, totalResults);

        List<WatchListItemResponseDto> pageItems = allItems.subList(fromIndex, toIndex).stream()
                .map(watchListItemMapper::toResponse)
                .toList();

        WatchListItemPageResponseDto response = new WatchListItemPageResponseDto();
        response.setPage(page);
        response.setTotalPages(totalPages);
        response.setTotalResults(totalResults);
        response.setItems(pageItems);
        return response;
    }

}
