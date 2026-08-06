package com.example.movies.client.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tmdb.api")
public class TmdbProperties {

    private String baseUrl;
    private String key;
}