package com.example.movies.mapper;

import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserEntity toEntity(UserRequestDto request);

    UserResponseDto toResponse(UserEntity user);

    List<UserResponseDto> toResponseList(List<UserEntity> users);
}
