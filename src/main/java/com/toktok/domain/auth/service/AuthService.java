package com.toktok.domain.auth.service;

import com.toktok.domain.auth.dto.request.LoginRequest;
import com.toktok.domain.auth.dto.request.ReissueRequest;
import com.toktok.domain.auth.dto.request.SignUpRequest;
import com.toktok.global.security.jwt.dto.Jwt;

public interface AuthService {

    void signup(SignUpRequest request);
    Jwt login(LoginRequest request);
    Jwt reissue(ReissueRequest request);
}
