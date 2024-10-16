package com.toktok.domain.user.error;

import com.toktok.global.error.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserError implements CustomError {
    USER_NOT_FOUND(400, "User not found"),
    USERNAME_DUPLICATE(400, "Username already exists"),
    EMAIL_DUPLICATION(400, "Email is already in use"),
    ;

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
