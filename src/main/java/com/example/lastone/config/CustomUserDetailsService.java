
package com.example.lastone.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.lastone.model.entity.UserEntity;
import com.example.lastone.repository.UserRepo;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;
    private final static String ROLE_PREFIX = "ROLE_";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = this.userRepo.findByUsername(username);
        user.orElseThrow(() -> new UsernameNotFoundException("User not found!"));
        String role[] = user.get().getRole().split(", ");
        Set<GrantedAuthority> roles = new HashSet<>();
        for (String userRole : role) {
            roles.add(new SimpleGrantedAuthority(ROLE_PREFIX+userRole));
        }
        return new CustomUserDetails(user.get().getUsername(), user.get().getPassword(), roles);
    }
}
