package com.toktok.domain.auth.service;

import com.toktok.domain.auth.dto.request.LoginRequest;
import com.toktok.domain.auth.dto.request.ReissueRequest;
import com.toktok.domain.auth.dto.request.SignUpRequest;
import com.toktok.domain.auth.error.AuthError;
import com.toktok.domain.auth.repository.RefreshTokenRepository;
import com.toktok.domain.user.domain.User;
import com.toktok.domain.user.domain.UserRole;
import com.toktok.domain.user.error.UserError;
import com.toktok.domain.user.repository.UserRepository;
import com.toktok.global.error.CustomException;
import com.toktok.global.security.jwt.dto.Jwt;
import com.toktok.global.security.jwt.enums.JwtType;
import com.toktok.global.security.jwt.error.JwtError;
import com.toktok.global.security.jwt.provider.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    @Override
    public void signup(SignUpRequest request) {
        String email = request.email();
        String password = request.password();

        if (userRepository.existsByEmail(email)) throw new CustomException(UserError.EMAIL_DUPLICATION);

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Jwt login(LoginRequest request) {
        String email = request.email();
        String password = request.password();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CustomException(AuthError.WRONG_PASSWORD);

        return jwtProvider.generateToken(email);
    }

    @Override
    public Jwt reissue(ReissueRequest request) {
        String refreshToken = request.refreshToken();

        if (jwtProvider.getType(refreshToken) != JwtType.REFRESH)
            throw new CustomException(JwtError.INVALID_TOKEN_TYPE);

        String email = jwtProvider.getSubject(refreshToken);

        if (!refreshTokenRepository.existsByEmail(email))
            throw new CustomException(JwtError.INVALID_REFRESH_TOKEN);

        if (!refreshTokenRepository.findByEmail(email).equals(refreshToken))
            throw new CustomException(JwtError.INVALID_REFRESH_TOKEN);

        if (!userRepository.existsByEmail(email)) throw new CustomException(UserError.USER_NOT_FOUND);

        return jwtProvider.generateToken(email);
    }
}
