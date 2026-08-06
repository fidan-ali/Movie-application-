package com.example.movies.service;

import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dao.repository.UserRepository;
import com.example.movies.dto.UserRequestDto;
import com.example.movies.dto.UserResponseDto;
import com.example.movies.exception.DuplicateResourceException;
import com.example.movies.exception.UserNotFoundException;
import com.example.movies.mapper.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponseDto createUser(UserRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }
        UserEntity user = userMapper.toEntity(request);
        UserEntity saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional
    public UserResponseDto updateUser(Long id, UserRequestDto request) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        if (userRepository.existsByEmailAndIdNot(request.getEmail(), id)) {
            throw new DuplicateResourceException("User", "email", request.getEmail());
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setBirthDate(request.getBirthDate());

        UserEntity updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    @Transactional
    public void deleteUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }

    public UserResponseDto findById(Long id) {
        UserEntity user = userRepository.findById(id).
                orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toResponse(user);
    }

    public List<UserResponseDto> getAll() {
        List<UserEntity> users = userRepository.findAll();
        return userMapper.toResponseList(users);

    }
}
