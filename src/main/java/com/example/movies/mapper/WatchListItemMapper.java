package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListItemEntity;
import com.example.movies.dto.WatchlistItemRequestDto;
import com.example.movies.dto.WatchlistItemResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WatchListItemMapper {
    WatchListItemEntity toEntity(WatchlistItemRequestDto request);

    WatchlistItemResponseDto toResponse(WatchListItemEntity watchListItem);
}