package com.example.movies.controller;

import static com.example.movies.constant.MovieApiTestConstants.BASE_USER_URL;
import static com.example.movies.constant.MovieApiTestConstants.BLANK_VALUE;
import static com.example.movies.constant.MovieApiTestConstants.EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.mockUserRequestDto;
import static com.example.movies.constant.MovieApiTestConstants.mockUserResponseDto;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.movies.exception.ErrorCode;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpHeaders;
import com.example.movies.service.UserService;
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

@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
@WebMvcTest(controllers = {UserController.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    private AuthenticatedUser principal() {
        return new AuthenticatedUser(ID, EMAIL, Role.ROLE_USER);
    }

    @Test
    void getUser_shouldReturnOk_whenUserExists() throws Exception {
        var response = mockUserResponseDto(ID);
        when(userService.getUserById(ID)).thenReturn(response);

        mockMvc.perform(get(BASE_USER_URL).with(user(principal())))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(userService).getUserById(ID);
    }


    @Test
    void updateUser_shouldReturnOk_whenRequestIsValid() throws Exception {
        var request = mockUserRequestDto();
        var response = mockUserResponseDto(ID);
        when(userService.updateUser(ID, request)).thenReturn(response);

        mockMvc.perform(put(BASE_USER_URL).with(user(principal()))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(userService).updateUser(ID, request);
    }

    @Test
    void updateUser_shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {
        var request = mockUserRequestDto();
        request.setFirstName(BLANK_VALUE);

        mockMvc.perform(put(BASE_USER_URL).with(user(principal()))
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUserById(ID);

        mockMvc.perform(delete(BASE_USER_URL).with(user(principal())))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(ID);
    }

    @Test
    void protectedEndpoint_shouldReturnUnauthorized_whenNoTokenIsProvided() throws Exception {
        mockMvc.perform(get(BASE_USER_URL))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.UNAUTHORIZED.name()))
                .andExpect(jsonPath("$.error.message").isNotEmpty());

        verifyNoInteractions(userService);
    }

    @Test
    void protectedEndpoint_shouldReturnUnauthorized_whenTokenIsInvalid() throws Exception {
        when(jwtService.parseAccessToken("garbage"))
                .thenThrow(new JwtException("invalid signature"));

        mockMvc.perform(get(BASE_USER_URL)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer garbage"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error.code").value(ErrorCode.UNAUTHORIZED.name()));

        verifyNoInteractions(userService);
    }
}
