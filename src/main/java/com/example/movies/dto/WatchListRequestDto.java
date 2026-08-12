package com.example.movies.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WatchListRequestDto {

    @NotBlank(message = "{validation.watchlist-name.required}")
    @Size(min = 1, max = 100, message = "{validation.watchlist-name.size}")
    private String name;
}