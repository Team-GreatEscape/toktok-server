package com.toktok.global.security.jwt.dto;

public record Jwt(
        String accessToken,
        String refreshToken
) {
}