package com.example.movies.dao.repository;


import com.example.movies.dao.entity.WatchListItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WatchListItemRepository extends JpaRepository<WatchListItem, Long> {
    List<WatchListItem> findAllByWatchlistId(Long watchlistId);

    boolean existsByWatchlistIdAndTmdbMovieId(Long watchlistId, Long tmdbMovieId);

}
