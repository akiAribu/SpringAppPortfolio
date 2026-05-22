package org.example.springappportfolio.services;

import lombok.RequiredArgsConstructor;
import org.example.springappportfolio.config.UserPrincipal;
import org.example.springappportfolio.models.User;
import org.example.springappportfolio.repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail)
                .orElseThrow( () -> new UsernameNotFoundException("User not found with username: " + usernameOrEmail) );

        System.out.println("Authorities: " + user.getUserRole().name());

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getUserPassword(),
                List.of(new SimpleGrantedAuthority(user.getUserRole().name()))
        );
    }
}
