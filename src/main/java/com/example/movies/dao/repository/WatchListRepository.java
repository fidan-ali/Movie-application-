package com.example.movies.dao.repository;


import com.example.movies.dao.entity.UserEntity;
import com.example.movies.dao.entity.WatchListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListRepository extends JpaRepository<WatchListEntity, Long> {

    boolean existsByUserEntityIdAndName(Long userId, String name);

    List<WatchListEntity> findAllByUserEntityId(Long userId);
}
