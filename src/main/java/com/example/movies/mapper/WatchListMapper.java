package com.example.movies.mapper;

import com.example.movies.dao.entity.WatchListEntity;
import com.example.movies.dto.WatchListRequestDto;
import com.example.movies.dto.WatchListResponseDto;
import com.example.movies.dto.WatchlistListResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


import java.util.List;

@Mapper
public interface WatchListMapper {
    WatchListMapper INSTANCE = Mappers.getMapper(WatchListMapper.class);

    WatchListEntity toEntity(WatchListRequestDto request);

    WatchListResponseDto toResponse(WatchListEntity watchList);

    List<WatchListResponseDto> toResponseList(List<WatchListEntity> watchLists);

    default WatchlistListResponseDto toListDto(List<WatchListEntity> watchLists) {
        return new WatchlistListResponseDto(toResponseList(watchLists));
    }
}