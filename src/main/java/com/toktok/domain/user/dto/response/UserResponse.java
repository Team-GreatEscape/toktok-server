package com.toktok.domain.user.dto.response;

import com.toktok.domain.user.domain.UserRole;

public record UserResponse(
        Long id,
        String email,
        UserRole role
) {
}