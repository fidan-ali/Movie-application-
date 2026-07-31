package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "watchlistitems")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchListItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "watchlist_id", nullable = false)
    private WatchList watchlist;

    @Column(name = "tmdb_movie_id", nullable = false)
    private Long tmdbMovieId;

    // Cached from TMDB at add-time, per spec section 6.3
    @Column(nullable = false)
    private String title;

    @ElementCollection
    //@CollectionTable(name = "watchlist_item_genres", joinColumns = @JoinColumn(name = "watchlist_item_id"))
    @Column(name = "genre_name")
    private List<String> genres = new ArrayList<>();

    @Column(name = "vote_average")
    private Double voteAverage;

    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

}

