package com.example.movies.dao.entity;

import com.example.movies.dao.entity.WatchList;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "watchlist_items")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
@ToString(exclude = "watchlist")
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

    @Column(nullable = false)
    private String title;

    @ElementCollection
    @CollectionTable(
            name = "watchlist_item_genres",
            joinColumns = @JoinColumn(name = "watchlist_item_id"))
    @Column(name = "genre_name")
    private List<String> genres = new ArrayList<>();

    @Column(name = "vote_average")
    private Double voteAverage;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false, updatable = false)
    private LocalDateTime addedAt;

}

