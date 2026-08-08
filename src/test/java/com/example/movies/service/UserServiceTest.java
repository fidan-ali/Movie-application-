package com.example.movies.service;

import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dao.repository.UserRepository;
import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import com.example.movies.exception.DuplicateResourceException;
import com.example.movies.exception.UserNotFoundException;
import com.example.movies.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.example.movies.constant.MovieApiTestConstants.EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserService service;

    @Test
    void shouldReturnUser() {
        UserEntity entity = new UserEntity();
        entity.setId(ID);

        UserResponseDto dto = new UserResponseDto();
        dto.setId(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(entity));
        when(mapper.toResponse(entity)).thenReturn(dto);

        UserResponseDto result = service.getUserById(ID);

        assertEquals(ID, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(repository.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> service.getUserById(ID));
    }

    @Test
    void shouldCreateUser() {
        UserRequestDto request = new UserRequestDto();
        request.setEmail(EMAIL);

        UserEntity entity = new UserEntity();

        UserEntity saved = new UserEntity();
        saved.setId(ID);

        UserResponseDto dto = new UserResponseDto();
        dto.setId(ID);

        when(repository.existsByEmail(request.getEmail())).thenReturn((false));
        when(mapper.toEntity(request)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(saved);
        when(mapper.toResponse(saved)).thenReturn(dto);
        UserResponseDto result = service.createUser(request);

        assertEquals(ID, result.getId());

    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        UserRequestDto request = new UserRequestDto();
        request.setEmail(EMAIL);

        when(repository.existsByEmail(request.getEmail()))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.createUser(request));
    }

    @Test
    void shouldUpdateUser() {
        UserRequestDto request = new UserRequestDto();
        request.setEmail(EMAIL);

        UserEntity entity = new UserEntity();
        entity.setId(ID);

        UserResponseDto dto = new UserResponseDto();
        dto.setId(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(entity));
        when(repository.existsByEmailAndIdNot(request.getEmail(), ID)).thenReturn(false);
        when(repository.save(entity)).thenReturn(entity);
        when(mapper.toResponse(entity)).thenReturn(dto);

        UserResponseDto result = service.updateUser(ID, request);

        assertEquals(ID, result.getId());
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingUser() {

        when(repository.findById(ID))
                .thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.updateUser(ID, new UserRequestDto()));
    }

    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {

        UserEntity entity = new UserEntity();

        UserRequestDto request = new UserRequestDto();
        request.setEmail(EMAIL);

        when(repository.findById(ID))
                .thenReturn(Optional.of(entity));

        when(repository.existsByEmailAndIdNot(request.getEmail(), ID))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class,
                () -> service.updateUser(ID, request));
    }

    @Test
    void shouldDeleteUser() {
        UserEntity entity = new UserEntity();
        when(repository.findById(ID))
                .thenReturn(Optional.of(entity));
        service.deleteUserById(ID);

        verify(repository).delete(entity);

    }

    @Test
    void shouldThrowExceptionWhenDeletingUnknownUser() {

        when(repository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> service.deleteUserById(ID));
    }

    @Test
    void shouldReturnAllUsers() {

        List<UserEntity> entities = List.of(new UserEntity());

        List<UserResponseDto> dtos = List.of(new UserResponseDto());

        when(repository.findAll())
                .thenReturn(entities);

        when(mapper.toResponseList(entities))
                .thenReturn(dtos);

        List<UserResponseDto> result = service.getAll();

        assertEquals(1, result.size());
    }
}
