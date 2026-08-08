package com.example.movies.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    // NotNull, NotBlank, NotEmpty ve s.
    @NotBlank(message = "")
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
}