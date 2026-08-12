package com.example.movies.client.config;

import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;

public class TmdbFeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new TmdbErrorDecoder();
    }

}
