package com.example.movies.mapper;

import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.List;

import static com.example.movies.constant.MovieApiTestConstants.BIRTH_DATE;
import static com.example.movies.constant.MovieApiTestConstants.EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.FIRST_INDEX;
import static com.example.movies.constant.MovieApiTestConstants.FIRST_NAME;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.ITEM_ID_1;
import static com.example.movies.constant.MovieApiTestConstants.ITEM_ID_2;
import static com.example.movies.constant.MovieApiTestConstants.LAST_NAME;
import static com.example.movies.constant.MovieApiTestConstants.SECOND_BIRTH_DATE;
import static com.example.movies.constant.MovieApiTestConstants.SECOND_EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.SECOND_FIRST_NAME;
import static com.example.movies.constant.MovieApiTestConstants.SECOND_INDEX;
import static com.example.movies.constant.MovieApiTestConstants.SECOND_LAST_NAME;
import static com.example.movies.constant.MovieApiTestConstants.TWO;
import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {

    private final UserMapper mapper =
            Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapRequestDtoToEntity() {

        UserRequestDto request =
                new UserRequestDto(FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE);

        UserEntity result = mapper.toEntity(request);

        assertEquals(FIRST_NAME, result.getFirstName());
        assertEquals(LAST_NAME, result.getLastName());
        assertEquals(EMAIL, result.getEmail());
        assertEquals(BIRTH_DATE, result.getBirthDate());
    }

    @Test
    void shouldMapEntityToResponseDto() {

        UserEntity user = new UserEntity();
        user.setId(ID);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setEmail(EMAIL);
        user.setBirthDate(BIRTH_DATE);

        UserResponseDto result = mapper.toResponse(user);

        assertEquals(ID, result.id());
        assertEquals(FIRST_NAME, result.firstName());
        assertEquals(LAST_NAME, result.lastName());
        assertEquals(EMAIL, result.email());
        assertEquals(BIRTH_DATE, result.birthDate());
    }

    @Test
    void shouldMapEntityListToResponseDtoList() {

        UserEntity user1 = new UserEntity();
        user1.setId(ITEM_ID_1);
        user1.setFirstName(FIRST_NAME);
        user1.setLastName(LAST_NAME);
        user1.setEmail(EMAIL);
        user1.setBirthDate(BIRTH_DATE);

        UserEntity user2 = new UserEntity();
        user2.setId(ITEM_ID_2);
        user2.setFirstName(SECOND_FIRST_NAME);
        user2.setLastName(SECOND_LAST_NAME);
        user2.setEmail(SECOND_EMAIL);
        user2.setBirthDate(SECOND_BIRTH_DATE);

        List<UserEntity> users = List.of(user1, user2);

        List<UserResponseDto> result =
                mapper.toResponseList(users);

        assertEquals(TWO, result.size());

        assertEquals(ITEM_ID_1, result.get(FIRST_INDEX).id());
        assertEquals(FIRST_NAME, result.get(FIRST_INDEX).firstName());

        assertEquals(ITEM_ID_2, result.get(SECOND_INDEX).id());
        assertEquals(SECOND_FIRST_NAME, result.get(SECOND_INDEX).firstName());
    }
}