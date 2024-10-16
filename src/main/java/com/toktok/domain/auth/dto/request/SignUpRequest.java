package com.toktok.domain.auth.dto.request;

import com.toktok.domain.user.domain.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(
        @NotBlank
        @Size(min = 3, max = 16)
        String username,
        @NotBlank
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 32)
        String password,
        @NotBlank
        Gender gender
) {
}