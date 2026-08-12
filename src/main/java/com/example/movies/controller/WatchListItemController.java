package com.example.movies.controller;

import com.example.movies.dto.WatchListItemPageResponseDto;
import com.example.movies.dto.WatchListItemRequestDto;
import com.example.movies.dto.WatchListItemResponseDto;
import com.example.movies.service.WatchListItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.example.movies.constant.Constant.HEADER_X_USER_ID;

@RestController
@RequestMapping("/api/v1/watchlists/{watchlistId}/items")
@RequiredArgsConstructor
public class WatchListItemController {
    private final WatchListItemService watchListItemService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WatchListItemResponseDto addItem(@PathVariable Long watchlistId,
                                            @RequestHeader(HEADER_X_USER_ID) Long userId,
                                            @Valid @RequestBody WatchListItemRequestDto request) {
        return watchListItemService.addItemToWatchlist(watchlistId, userId, request);
    }

    @DeleteMapping("/{itemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteItem(@PathVariable Long watchlistId,
                           @RequestHeader(HEADER_X_USER_ID) Long userId,
                           @PathVariable Long itemId) {
        watchListItemService.deleteWatchListItem(watchlistId, userId, itemId);
    }

    @GetMapping
    public WatchListItemPageResponseDto getItems(@PathVariable Long watchlistId,
                                                 @RequestHeader(HEADER_X_USER_ID) Long userId,
                                                 @RequestParam(defaultValue = "1") int page,
                                                 @RequestParam(value = "page_size", defaultValue = "20") int pageSize) {
        return watchListItemService.getWatchListItems(watchlistId, userId, page, pageSize);
    }
}