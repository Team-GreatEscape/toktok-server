package com.toktok.domain.user.service;

import com.toktok.domain.user.domain.User;
import com.toktok.domain.user.dto.response.UserResponse;
import com.toktok.domain.user.error.UserError;
import com.toktok.domain.user.repository.UserRepository;
import com.toktok.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserResponse getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));

        return new UserResponse(
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getRole()
        );
    }
}
