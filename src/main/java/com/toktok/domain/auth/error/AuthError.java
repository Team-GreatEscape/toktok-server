package com.toktok.domain.auth.error;

import com.toktok.global.error.CustomError;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AuthError implements CustomError {

    WRONG_PASSWORD(400, "Wrong password"),
    ;

    private final int status;
    private final String message;

    @Override
    public String getCode() {
        return name();
    }
}
