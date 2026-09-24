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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.example.movies.constant.Constant.HEADER_X_USER_ID;

@RestController
@RequestMapping("/api/v1/watchlists")
@RequiredArgsConstructor
public class WatchListController {

    private final WatchListService watchListService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WatchListResponseDto createWatchList(
            @RequestHeader(HEADER_X_USER_ID) Long userId, // magic string
            @Valid @RequestBody WatchListRequestDto request) {

        return watchListService.createWatchList(userId, request);
    }

    @GetMapping
    public WatchlistListResponseDto getUserWatchLists(
            @RequestHeader(HEADER_X_USER_ID) Long userId) {

        return watchListService.getUserWatchLists(userId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWatchList(
            @PathVariable Long id,
            @RequestHeader(HEADER_X_USER_ID) Long userId) {

        watchListService.deleteWatchList(id, userId);
    }
}