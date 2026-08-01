package com.example.movies.dao.repository;


import com.example.movies.dao.entity.WatchList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListRepository extends JpaRepository<WatchList, Long> {
    List<WatchList> findAllByUserId(Long userId);

    boolean existsByUserIdAndName(Long userId, String name);
}
