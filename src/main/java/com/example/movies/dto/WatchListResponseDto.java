// WatchListResponseDto.java
package com.example.movies.dto;

import java.time.LocalDateTime;

public record WatchListResponseDto(
        Long id,
        Long userId,
        String name,
        LocalDateTime createdAt
) {
}