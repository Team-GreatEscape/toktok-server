package com.toktok.domain.auth.service;

import com.toktok.domain.auth.dto.request.LoginRequest;
import com.toktok.domain.auth.dto.request.ReissueRequest;
import com.toktok.domain.auth.dto.request.SignUpRequest;
import com.toktok.domain.auth.error.AuthError;
import com.toktok.domain.auth.repository.RefreshTokenRepository;
import com.toktok.domain.user.domain.Gender;
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
        String username = request.username();
        String email = request.email();
        String password = request.password();
        Gender gender = request.gender();

        if (userRepository.existsByEmail(email)) throw new CustomException(UserError.EMAIL_DUPLICATION);
        if (userRepository.existsByUsername(username)) throw new CustomException(UserError.USERNAME_DUPLICATE);

        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .gender(gender)
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public Jwt login(LoginRequest request) {
        String username = request.username();
        String password = request.password();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new CustomException(AuthError.WRONG_PASSWORD);

        return jwtProvider.generateToken(username);
    }

    @Override
    public Jwt reissue(ReissueRequest request) {
        String refreshToken = request.refreshToken();

        if (jwtProvider.getType(refreshToken) != JwtType.REFRESH)
            throw new CustomException(JwtError.INVALID_TOKEN_TYPE);

        String username = jwtProvider.getSubject(refreshToken);

        if (!refreshTokenRepository.existsByEmail(username))
            throw new CustomException(JwtError.INVALID_REFRESH_TOKEN);

        if (!refreshTokenRepository.findByEmail(username).equals(refreshToken))
            throw new CustomException(JwtError.INVALID_REFRESH_TOKEN);

        if (!userRepository.existsByUsername(username)) throw new CustomException(UserError.USER_NOT_FOUND);

        return jwtProvider.generateToken(username);
    }
}
