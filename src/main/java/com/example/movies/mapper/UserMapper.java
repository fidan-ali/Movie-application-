package com.example.movies.mapper;

import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserEntity toEntity(UserRequestDto request);

    UserResponseDto toResponse(UserEntity user);

    List<UserResponseDto> toResponseList(List<UserEntity> users);
}
