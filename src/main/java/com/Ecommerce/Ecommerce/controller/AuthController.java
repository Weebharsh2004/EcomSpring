package com.Ecommerce.Ecommerce.controller;

import com.Ecommerce.Ecommerce.dataTransfer.auth.AuthRequestDTO;
import com.Ecommerce.Ecommerce.dataTransfer.auth.AuthResponseDTO;
import com.Ecommerce.Ecommerce.dataTransfer.user.UserDTO;
import com.Ecommerce.Ecommerce.model.actors.Role;
import com.Ecommerce.Ecommerce.model.actors.User;
import com.Ecommerce.Ecommerce.repository.user.UserRepository;
import com.Ecommerce.Ecommerce.security.CustomUserDetailsService;
import com.Ecommerce.Ecommerce.security.JwtUtil;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, JwtUtil jwtUtil,
                          CustomUserDetailsService userDetailsService, UserRepository userRepository,
                          PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword())); // Hash password
        user.setRole(userDTO.getRole() != null ? userDTO.getRole() : Role.USER); // Default role USER

        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody AuthRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthResponseDTO(token));
    }
}
