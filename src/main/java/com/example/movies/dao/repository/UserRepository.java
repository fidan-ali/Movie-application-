package com.example.movies.dao.repository;



import com.example.movies.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByEmail(String email);
    Boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}
