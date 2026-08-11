package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListItemEntity;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface WatchListItemMapper {
    WatchListItemMapper INSTANCE = Mappers.getMapper(WatchListItemMapper.class);

    WatchListItemEntity toEntity(WatchListItemRequestDto request);

    WatchListItemResponseDto toResponse(WatchListItemEntity watchListItem);
}