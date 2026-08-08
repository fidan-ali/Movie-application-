package com.example.movies.controller;

import com.example.movies.dto.WatchListRequestDto;
import com.example.movies.dto.WatchListResponseDto;
import com.example.movies.dto.WatchlistListResponseDto;
import com.example.movies.service.WatchListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class WatchListController {
    WatchListService watchListService;

    @PostMapping("/api/v1/users/{userId}/watchlists")
    public ResponseEntity<WatchListResponseDto> createWatchList(@PathVariable Long userId,
                                                                @Valid @RequestBody WatchListRequestDto request){
        WatchListResponseDto response = watchListService.createWatchList(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("GET /api/v1/users/{userId}/watchlists")
    public ResponseEntity<WatchlistListResponseDto> getUserWatchLists(@PathVariable Long userId){
        return ResponseEntity.ok(watchListService.getUserWatchLists(userId));
    }

    @DeleteMapping("/api/v1/watchlists/{id}")
    public ResponseEntity<Void> deleteWatchList(@PathVariable Long id) {
        watchListService.deleteWatchList(id);
        return ResponseEntity.noContent().build();
    }
}
