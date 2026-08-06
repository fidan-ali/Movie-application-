package com.example.movies.client.config;

import com.example.movies.exception.TmdbApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class TmdbErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response){
        int status = response.status();

        switch(status){
            case 404:
                return new TmdbApiException(
                        "Resource not found on TMDB.",
                        status,
                        null
                );
            case 401:
                return new TmdbApiException(
                        "Invalid TMDB API key.",
                        status,
                        null
                );

            case 429:
                return new TmdbApiException(
                        "TMDB rate limit exceeded.",
                        status,
                        null
                );

            default:
                return new TmdbApiException(
                        "TMDB request failed.",
                        status,
                        null
                );
        }

    }
}
