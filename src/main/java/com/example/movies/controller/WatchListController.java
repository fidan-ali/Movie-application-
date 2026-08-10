package com.example.movies.controller;

import com.example.movies.dto.WatchListRequestDto;
import com.example.movies.dto.WatchListResponseDto;
import com.example.movies.dto.WatchlistListResponseDto;
import com.example.movies.service.WatchListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class WatchListController {

    private final WatchListService watchListService;

    @PostMapping("/users/{userId}/watchlists")
    @ResponseStatus(HttpStatus.CREATED)
    public WatchListResponseDto createWatchList(
            @PathVariable Long userId,
            @Valid @RequestBody WatchListRequestDto request) {

        return watchListService.createWatchList(userId, request);
    }

    @GetMapping("/users/{userId}/watchlists")
    public WatchlistListResponseDto getUserWatchLists(
            @PathVariable Long userId) {

        return watchListService.getUserWatchLists(userId);
    }

    @DeleteMapping("/watchlists/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWatchList(@PathVariable Long id) {
        watchListService.deleteWatchList(id);
    }
}