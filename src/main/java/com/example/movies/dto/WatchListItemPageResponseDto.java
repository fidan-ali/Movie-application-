package com.example.movies.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchListItemPageResponseDto {
    private int page;
    private int totalPages;
    private int totalResults;
    private List<WatchListItemResponseDto> items;
}
