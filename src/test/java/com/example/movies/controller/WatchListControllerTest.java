package com.example.movies.controller;

import com.example.movies.dto.WatchlistListResponseDto;
import com.example.movies.service.WatchListService;
import com.example.movies.config.SecurityConfig;
import com.example.movies.security.JwtService;
import com.example.movies.security.RestAccessDeniedHandler;
import com.example.movies.security.RestAuthenticationEntryPoint;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.example.movies.constant.Role;
import com.example.movies.security.AuthenticatedUser;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.example.movies.constant.MovieApiTestConstants.BASE_WATCHLIST_URL;
import static com.example.movies.constant.MovieApiTestConstants.NON_NUMERIC_ID;
import static com.example.movies.constant.MovieApiTestConstants.EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.USER_ID;
import static com.example.movies.constant.MovieApiTestConstants.WATCHLIST_ID;
import static com.example.movies.constant.MovieApiTestConstants.WATCHLIST_URL;
import static com.example.movies.constant.MovieApiTestConstants.BLANK_VALUE;
import static com.example.movies.constant.MovieApiTestConstants.mockWatchListRequestDto;
import static com.example.movies.constant.MovieApiTestConstants.mockWatchListResponseDto;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
@WebMvcTest(WatchListController.class)
class WatchListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private WatchListService watchListService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthenticatedUser principal() {
        return new AuthenticatedUser(USER_ID, EMAIL, Role.ROLE_USER);
    }

    @Test
    void createWatchList_shouldReturnCreated_whenRequestIsValid()
            throws Exception {

        var request = mockWatchListRequestDto();
        var response = mockWatchListResponseDto();

        when(watchListService.createWatchList(USER_ID, request))
                .thenReturn(response);

        mockMvc.perform(post(BASE_WATCHLIST_URL).with(user(principal()))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(response)
                ));

        verify(watchListService).createWatchList(USER_ID, request);
    }

    @Test
    void createWatchList_shouldReturnBadRequest_whenNameIsBlank()
            throws Exception {

        var request = mockWatchListRequestDto();
        request.setName(BLANK_VALUE);

        mockMvc.perform(post(BASE_WATCHLIST_URL).with(user(principal()))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListService);
    }

    @Test
    void createWatchList_shouldReturnBadRequest_whenBodyIsMissing()
            throws Exception {

        mockMvc.perform(post(BASE_WATCHLIST_URL).with(user(principal()))
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListService);
    }


    @Test
    void getUserWatchLists_shouldReturnOk_whenWatchListsExist()
            throws Exception {

        var watchList = mockWatchListResponseDto();

        var response = new WatchlistListResponseDto(
                List.of(watchList)
        );

        when(watchListService.getUserWatchLists(USER_ID))
                .thenReturn(response);

        mockMvc.perform(get(BASE_WATCHLIST_URL).with(user(principal())))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        objectMapper.writeValueAsString(response)
                ));

        verify(watchListService).getUserWatchLists(USER_ID);
    }


    @Test
    void deleteWatchList_shouldReturnNoContent() throws Exception {

        doNothing().when(watchListService).deleteWatchList(WATCHLIST_ID, USER_ID);

        mockMvc.perform(delete(WATCHLIST_URL, WATCHLIST_ID)
                        .with(user(principal())))
                .andExpect(status().isNoContent());

        verify(watchListService).deleteWatchList(WATCHLIST_ID, USER_ID);
    }

    @Test
    void deleteWatchList_shouldReturnBadRequest_whenIdIsNotNumeric()
            throws Exception {

        mockMvc.perform(delete(WATCHLIST_URL, NON_NUMERIC_ID).with(user(principal())))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(watchListService);
    }
}