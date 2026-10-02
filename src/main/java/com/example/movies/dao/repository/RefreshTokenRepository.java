package com.example.movies.dao.repository;

import com.example.movies.dao.entity.RefreshTokenEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select r from RefreshTokenEntity r where r.token = :token")
  Optional<RefreshTokenEntity> findByTokenForUpdate(@Param("token") String token);
}