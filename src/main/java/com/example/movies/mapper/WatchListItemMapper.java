package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListItemEntity;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WatchListItemMapper {
    WatchListItemEntity toEntity(WatchListItemRequestDto request);

    WatchListItemResponseDto toResponse(WatchListItemEntity watchListItem);
}