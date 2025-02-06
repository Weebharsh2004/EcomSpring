package com.Ecommerce.Ecommerce.security;

import com.Ecommerce.Ecommerce.model.actors.User;
import com.Ecommerce.Ecommerce.repository.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository=userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByEmail(email);

        return user.map(CustomUserDetails::new).orElseThrow(()-> new UsernameNotFoundException("User not found"));
    }
}
