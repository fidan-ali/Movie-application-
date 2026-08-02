package com.example.movies.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "tmdb.api")
public class TmdbProperties {

    private String baseUrl;
    private String key;
}