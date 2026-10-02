package com.example.movies.service;

import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dao.repository.UserRepository;
import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import com.example.movies.exception.DuplicateResourceException;
import com.example.movies.exception.UserNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void shouldReturnUser() {
        UserEntity entity = new UserEntity();
        entity.setId(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(entity));

        UserResponseDto result = service.getUserById(ID);

        assertEquals(ID, result.id());
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        when(repository.findById(ID))
                .thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class,
                () -> service.getUserById(ID));
    }

    @Test
    void shouldUpdateUser() {
        UserRequestDto request = new UserRequestDto();
        request.setEmail(EMAIL);

        UserEntity entity = new UserEntity();
        entity.setId(ID);

        when(repository.findById(ID)).thenReturn(Optional.of(entity));
        when(repository.existsByEmailAndIdNot(request.getEmail(), ID)).thenReturn(false);
        when(repository.save(entity)).thenReturn(entity);

        UserResponseDto result = service.updateUser(ID, request);

        assertEquals(ID, result.id());
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

        when(repository.findAll())
                .thenReturn(entities);

        List<UserResponseDto> result = service.getAll();

        assertEquals(1, result.size());
    }
}