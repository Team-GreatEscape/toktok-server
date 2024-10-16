package com.toktok.domain.user.dto.response;

import com.toktok.domain.user.domain.Gender;
import com.toktok.domain.user.domain.UserRole;

public record UserResponse(
        String username,
        String email,
        Gender gender,
        UserRole role
) {
}