package com.example.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MovieApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(MovieApiApplication.class, args);
    }
}