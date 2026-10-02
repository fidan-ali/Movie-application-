package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListItemEntity;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WatchListItemMapper {
    WatchListItemMapper INSTANCE = Mappers.getMapper(WatchListItemMapper.class);

    WatchListItemEntity toEntity(WatchListItemRequestDto request);

    @Mapping(target = "watchlistId", source = "watchlist.id")
    WatchListItemResponseDto toResponse(WatchListItemEntity watchListItem);
}