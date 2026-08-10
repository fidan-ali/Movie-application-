package com.example.movies.controller;

import static com.example.movies.constant.MovieApiTestConstants.BASE_USER_URL;
import static com.example.movies.constant.MovieApiTestConstants.BLANK_VALUE;
import static com.example.movies.constant.MovieApiTestConstants.FUTURE_BIRTH_DATE;
import static com.example.movies.constant.MovieApiTestConstants.ID;
import static com.example.movies.constant.MovieApiTestConstants.INVALID_EMAIL;
import static com.example.movies.constant.MovieApiTestConstants.TOO_SHORT_NAME;
import static com.example.movies.constant.MovieApiTestConstants.mockUserRequestDto;
import static com.example.movies.constant.MovieApiTestConstants.mockUserResponseDto;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.movies.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {UserController.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createUser_shouldReturnCreated_whenRequestIsValid() throws Exception {
        var request = mockUserRequestDto();
        var response = mockUserResponseDto(ID);
        when(userService.createUser(request)).thenReturn(response);

        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(userService).createUser(request);
    }

    @Test
    void createUser_shouldReturnBadRequest_whenFirstNameIsBlank() throws Exception {
        var request = mockUserRequestDto();
        request.setFirstName(BLANK_VALUE);

        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void createUser_shouldReturnBadRequest_whenFirstNameTooShort() throws Exception {
        var request = mockUserRequestDto();
        request.setFirstName(TOO_SHORT_NAME);

        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturnBadRequest_whenLastNameIsBlank() throws Exception {
        var request = mockUserRequestDto();
        request.setLastName(BLANK_VALUE);

        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturnBadRequest_whenEmailIsInvalid() throws Exception {
        var request = mockUserRequestDto();
        request.setEmail(INVALID_EMAIL);

        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturnBadRequest_whenEmailIsBlank() throws Exception {
        var request = mockUserRequestDto();
        request.setEmail(BLANK_VALUE);

        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturnBadRequest_whenBirthDateIsMissing() throws Exception {
        var request = mockUserRequestDto();
        request.setBirthDate(null);

        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturnBadRequest_whenBirthDateIsInFuture() throws Exception {
        var request = mockUserRequestDto();
        request.setBirthDate(FUTURE_BIRTH_DATE);

        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createUser_shouldReturnBadRequest_whenBodyIsMissing() throws Exception {
        mockMvc.perform(post(BASE_USER_URL)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getUser_shouldReturnOk_whenUserExists() throws Exception {
        var response = mockUserResponseDto(ID);
        when(userService.getUserById(ID)).thenReturn(response);

        mockMvc.perform(get(BASE_USER_URL + "/{id}", ID))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(response)));

        verify(userService).getUserById(ID);
    }

    @Test
    void getUser_shouldReturnBadRequest_whenIdIsNotNumeric() throws Exception {
        mockMvc.perform(get(BASE_USER_URL + "/{id}", "abc"))
                .andExpect(status().isBadRequest());
    }


    @Test
    void updateUser_shouldReturnOk_whenRequestIsValid() throws Exception {
        var request = mockUserRequestDto();
        var response = mockUserResponseDto(ID);
        when(userService.updateUser(ID, request)).thenReturn(response);

        mockMvc.perform(put(BASE_USER_URL + "/{id}", ID)
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

        mockMvc.perform(put(BASE_USER_URL + "/{id}", ID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        doNothing().when(userService).deleteUserById(ID);

        mockMvc.perform(delete(BASE_USER_URL + "/{id}", ID))
                .andExpect(status().isNoContent());

        verify(userService).deleteUserById(ID);
    }
}