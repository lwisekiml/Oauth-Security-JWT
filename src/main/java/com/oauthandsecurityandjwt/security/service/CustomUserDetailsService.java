package com.oauthandsecurityandjwt.security.service;

import com.oauthandsecurityandjwt.security.Repository.UserRepository;
import com.oauthandsecurityandjwt.security.dto.CustomUserDetails;
import com.oauthandsecurityandjwt.security.entity.UserEntity;
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
        UserEntity userData = userRepository.findByUsername(username);

        if (userData == null) {
            return null;
        }

        return new CustomUserDetails(userData);
    }
}
