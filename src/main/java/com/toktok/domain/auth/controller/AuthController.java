package com.toktok.domain.auth.controller;

import com.toktok.domain.auth.dto.request.LoginRequest;
import com.toktok.domain.auth.dto.request.ReissueRequest;
import com.toktok.domain.auth.dto.request.SignUpRequest;
import com.toktok.domain.auth.service.AuthService;
import com.toktok.global.common.BaseResponse;
import com.toktok.global.security.jwt.dto.Jwt;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<BaseResponse<Void>> signup(@RequestBody SignUpRequest request) {
        authService.signup(request);

        return BaseResponse.of(null, 201);
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<Jwt>> login(@RequestBody LoginRequest request) {
        return BaseResponse.of(authService.login(request), 201);
    }

    @PostMapping("/reissue")
    public ResponseEntity<BaseResponse<Jwt>> reissue(@RequestBody ReissueRequest request) {
        return BaseResponse.of(authService.reissue(request), 201);
    }
}
