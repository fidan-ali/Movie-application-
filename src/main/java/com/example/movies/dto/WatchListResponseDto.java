package com.example.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchListResponseDto {
    private Long id;
    private Long userId;
    private String name;
    private LocalDateTime createdAt;
}
