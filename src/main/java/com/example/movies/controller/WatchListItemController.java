package com.example.movies.controller;

import com.example.movies.dto.WatchListItemPageResponseDto;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import com.example.movies.service.WatchListItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/watchlists/{watchlistId}/items")
@RequiredArgsConstructor
public class WatchListItemController {
    private final WatchListItemService watchListItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WatchListItemResponseDto addItem(@PathVariable Long watchlistId,
                                            @Valid @RequestBody WatchListItemRequestDto request) {
        return watchListItemService.addItemToWatchlist(watchlistId, request);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long watchlistId, @PathVariable Long itemId) {
        watchListItemService.deleteWatchListItem(watchlistId, itemId);
    }

    @GetMapping
    public WatchListItemPageResponseDto getItems(@PathVariable Long watchlistId,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(value = "page_size", defaultValue = "20") int pageSize) {
        return watchListItemService.getWatchListItems(watchlistId, page, pageSize);
    }
}