package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListEntity;
import com.example.movies.dto.WatchlistListResponseDto;
import com.example.movies.dto.WatchListRequestDto;
import com.example.movies.dto.WatchListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WatchListMapper {
    WatchListEntity toEntity(WatchListRequestDto request);

    WatchListResponseDto toResponse(WatchListEntity watchList);

    @Mapping(target = "watchlists", source = "watchLists")
    WatchlistListResponseDto toListDto(List<WatchListEntity> watchLists);
}