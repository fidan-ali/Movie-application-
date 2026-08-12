package com.example.movies.dao.repository;


import com.example.movies.dao.entity.WatchListItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListItemRepository extends JpaRepository<WatchListItemEntity, Long> {
    List<WatchListItemEntity> findAllByWatchlistId(Long watchlistId);

    boolean existsByWatchlistIdAndTmdbMovieId(Long watchlistId, Long tmdbMovieId);

}
