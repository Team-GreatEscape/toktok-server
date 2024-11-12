package com.toktok.global.security.details;

import com.toktok.domain.user.domain.User;
import com.toktok.domain.user.error.UserError;
import com.toktok.domain.user.repository.UserRepository;
import com.toktok.global.error.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(UserError.USER_NOT_FOUND));

        return new CustomUserDetails(user);
    }
}
