package com.example.movies.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Getter
@Setter
@RefreshScope
@Component
@ConfigurationProperties(prefix = "tmdb.cache")
public class TmdbCacheProperties {
    private long popularMoviesTtlMinutes;
    private long movieDetailsTtlMinutes;
    private long genresTtlMinutes;
}