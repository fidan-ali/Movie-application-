package com.example.movies.controller;

import com.example.movies.service.WatchListItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.example.movies.constant.MovieApiTestConstants.BASE_WATCHLIST_ITEM_URL;
import static com.example.movies.constant.MovieApiTestConstants.DEFAULT_PAGE_SIZE;
import static com.example.movies.constant.MovieApiTestConstants.FIRST_PAGE;
import static com.example.movies.constant.MovieApiTestConstants.ITEM_ID_1;
import static com.example.movies.constant.MovieApiTestConstants.NON_NUMERIC_ID;
import static com.example.movies.constant.MovieApiTestConstants.PAGE_PARAM;
import static com.example.movies.constant.MovieApiTestConstants.PAGE_SIZE;
import static com.example.movies.constant.MovieApiTestConstants.PAGE_SIZE_PARAM;
import static com.example.movies.constant.MovieApiTestConstants.USER_ID;
import static com.example.movies.constant.MovieApiTestConstants.WATCHLIST_ID;
import static com.example.movies.constant.MovieApiTestConstants.WATCHLIST_ITEM_URL;
import static com.example.movies.constant.MovieApiTestConstants.ZERO;
import static com.example.movies.constant.MovieApiTestConstants.mockWatchListItemPageResponseDto;
import static com.example.movies.constant.MovieApiTestConstants.mockWatchListItemRequestDto;
import static com.example.movies.constant.MovieApiTestConstants.mockWatchListItemResponseDto;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WatchListItemController.class)
class WatchListItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WatchListItemService watchListItemService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addItem_shouldReturnCreated_whenRequestIsValid()
            throws Exception {

        var request = mockWatchListItemRequestDto();
        var response = mockWatchListItemResponseDto();

        when(watchListItemService.addItemToWatchlist(WATCHLIST_ID, request))
                .thenReturn(response);

        mockMvc.perform(post(BASE_WATCHLIST_ITEM_URL, WATCHLIST_ID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(response)
                ));

        verify(watchListItemService)
                .addItemToWatchlist(WATCHLIST_ID, request);
    }

    @Test
    void addItem_shouldReturnBadRequest_whenTmdbMovieIdIsMissing()
            throws Exception {

        var request = mockWatchListItemRequestDto();
        request.setTmdbMovieId(null);

        mockMvc.perform(post(BASE_WATCHLIST_ITEM_URL, WATCHLIST_ID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListItemService);
    }

    @Test
    void addItem_shouldReturnBadRequest_whenTmdbMovieIdIsNotPositive()
            throws Exception {

        var request = mockWatchListItemRequestDto();
        request.setTmdbMovieId(ZERO);

        mockMvc.perform(post(BASE_WATCHLIST_ITEM_URL, WATCHLIST_ID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListItemService);
    }

    @Test
    void addItem_shouldReturnBadRequest_whenWatchlistIdIsNotNumeric()
            throws Exception {

        var request = mockWatchListItemRequestDto();

        mockMvc.perform(post(BASE_WATCHLIST_ITEM_URL, NON_NUMERIC_ID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListItemService);
    }

    @Test
    void deleteItem_shouldReturnNoContent() throws Exception {

        mockMvc.perform(
                        delete(
                                WATCHLIST_ITEM_URL,
                                WATCHLIST_ID,
                                ITEM_ID_1
                        )
                )
                .andExpect(status().isNoContent());

        verify(watchListItemService)
                .deleteWatchListItem(WATCHLIST_ID, ITEM_ID_1);
    }

    @Test
    void deleteItem_shouldReturnBadRequest_whenWatchlistIdIsNotNumeric()
            throws Exception {

        mockMvc.perform(
                        delete(
                                WATCHLIST_ITEM_URL,
                                NON_NUMERIC_ID,
                                ITEM_ID_1
                        )
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListItemService);
    }

    @Test
    void deleteItem_shouldReturnBadRequest_whenItemIdIsNotNumeric()
            throws Exception {

        mockMvc.perform(
                        delete(
                                WATCHLIST_ITEM_URL,
                                WATCHLIST_ID,
                                NON_NUMERIC_ID
                        )
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListItemService);
    }

    @Test
    void getItems_shouldReturnOk_whenItemsExist()
            throws Exception {

        var response = mockWatchListItemPageResponseDto();

        when(watchListItemService.getWatchListItems(
                WATCHLIST_ID,
                FIRST_PAGE,
                PAGE_SIZE
        )).thenReturn(response);

        mockMvc.perform(
                        get(BASE_WATCHLIST_ITEM_URL, WATCHLIST_ID)
                                .param(
                                        PAGE_PARAM,
                                        String.valueOf(FIRST_PAGE)
                                )
                                .param(
                                        PAGE_SIZE_PARAM,
                                        String.valueOf(PAGE_SIZE)
                                )
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(response)
                ));

        verify(watchListItemService)
                .getWatchListItems(
                        WATCHLIST_ID,
                        FIRST_PAGE,
                        PAGE_SIZE
                );
    }

    @Test
    void getItems_shouldReturnOk_whenDefaultPaginationIsUsed()
            throws Exception {

        var response = mockWatchListItemPageResponseDto();

        when(watchListItemService.getWatchListItems(
                WATCHLIST_ID,
                FIRST_PAGE,
                DEFAULT_PAGE_SIZE
        )).thenReturn(response);

        mockMvc.perform(
                        get(BASE_WATCHLIST_ITEM_URL, WATCHLIST_ID)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(response)
                ));

        verify(watchListItemService)
                .getWatchListItems(
                        WATCHLIST_ID,
                        FIRST_PAGE,
                        DEFAULT_PAGE_SIZE
                );
    }

    @Test
    void getItems_shouldReturnBadRequest_whenWatchlistIdIsNotNumeric()
            throws Exception {

        mockMvc.perform(
                        get(BASE_WATCHLIST_ITEM_URL, NON_NUMERIC_ID)
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListItemService);
    }
}