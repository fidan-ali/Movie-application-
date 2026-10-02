package com.example.movies.controller;

import com.example.movies.dto.RegisterRequestDto;
import com.example.movies.service.AuthService;
import com.example.movies.config.SecurityConfig;
import com.example.movies.security.JwtService;
import com.example.movies.security.RestAccessDeniedHandler;
import com.example.movies.security.RestAuthenticationEntryPoint;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static com.example.movies.constant.MovieApiTestConstants.BIRTH_DATE;
import static com.example.movies.constant.MovieApiTestConstants.BLANK_VALUE;
import static com.example.movies.constant.MovieApiTestConstants.EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.FIRST_NAME;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.INVALID_EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.LAST_NAME;
import static com.example.movies.constant.MovieApiTestConstants.TOO_SHORT_NAME;
import static com.example.movies.constant.MovieApiTestConstants.mockUserResponseDto;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, RestAuthenticationEntryPoint.class, RestAccessDeniedHandler.class})
@WebMvcTest(controllers = {AuthController.class})
class AuthControllerTest {

    private static final String REGISTER_URL = "/api/v1/auth/register";
    private static final String VALID_PASSWORD = "Password123";
    private static final String SHORT_PASSWORD = "123";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private UserDetailsService userDetailsService;


    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequestDto validRequest() {
        return new RegisterRequestDto(
                FIRST_NAME, LAST_NAME, EMAIL, BIRTH_DATE, VALID_PASSWORD);
    }

    @Test
    void register_shouldReturnCreated_whenRequestIsValid() throws Exception {
        var request = validRequest();
        var response = mockUserResponseDto(ID);
        when(authService.register(request)).thenReturn(response);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(authService).register(request);
    }

    @Test
    void register_shouldReturnBadRequest_whenPasswordIsTooShort() throws Exception {
        var request = validRequest();
        request.setPassword(SHORT_PASSWORD);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void register_shouldReturnBadRequest_whenPasswordIsBlank() throws Exception {
        var request = validRequest();
        request.setPassword(BLANK_VALUE);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void register_shouldReturnBadRequest_whenEmailIsInvalid() throws Exception {
        var request = validRequest();
        request.setEmail(INVALID_EMAIL);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void register_shouldReturnBadRequest_whenFirstNameIsTooShort() throws Exception {
        var request = validRequest();
        request.setFirstName(TOO_SHORT_NAME);

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void register_shouldReturnBadRequest_whenBirthDateIsInFuture() throws Exception {
        var request = validRequest();
        request.setBirthDate(LocalDate.now().plusDays(1));

        mockMvc.perform(post(REGISTER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }

    @Test
    void register_shouldReturnBadRequest_whenBodyIsMissing() throws Exception {
        mockMvc.perform(post(REGISTER_URL)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(authService);
    }
}
