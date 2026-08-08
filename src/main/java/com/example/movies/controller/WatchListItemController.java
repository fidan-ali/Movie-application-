package com.example.movies.controller;

import com.example.movies.dto.WatchListItemPageResponseDto;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import com.example.movies.service.WatchListItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/watchlists/{watchlistId}/items")
@RequiredArgsConstructor
public class WatchListItemController {
    private final WatchListItemService watchListItemService;

    @PostMapping
    public ResponseEntity<WatchListItemResponseDto> addItem(@PathVariable Long watchlistId,
                                                            @Valid @RequestBody WatchListItemRequestDto request) {
        WatchListItemResponseDto response = watchListItemService.addItemToWatchlist(watchlistId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long watchlistId, @PathVariable Long itemId) {
        watchListItemService.deleteWatchListItem(watchlistId, itemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<WatchListItemPageResponseDto> getItems(@PathVariable Long watchlistId,
                                                                 @RequestParam(defaultValue = "1") int page,
                                                                 @RequestParam(value = "page_size", defaultValue = "20") int pageSize) {
        return ResponseEntity.ok(watchListItemService.getWatchListItems(watchlistId, page, pageSize));
    }

}
