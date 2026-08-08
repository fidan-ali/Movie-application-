// TmdbErrorDecoder.java
package com.example.movies.client.config;

import com.example.movies.exception.TmdbApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TmdbErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();

        switch (status) {
            case 404:
                log.warn("TMDB 404 on {}: resource not found", methodKey);
                break;
            case 401:
                log.error("TMDB 401 on {}: invalid API key", methodKey);
                break;
            case 429:
                log.warn("TMDB 429 on {}: rate limit exceeded", methodKey);
                break;
            default:
                log.error("TMDB {} on {}: request failed", status, methodKey);
        }

        return new TmdbApiException(status, null);
    }
}