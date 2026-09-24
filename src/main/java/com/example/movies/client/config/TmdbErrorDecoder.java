package com.example.movies.client.config;

import com.example.movies.exception.TmdbApiException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class TmdbErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        return new TmdbApiException(response.status(), null);
    }
}