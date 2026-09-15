package com.example.demo.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.AuthResponse;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public String register(RegisterRequest request) {

        if (userRepository.existsByEmail(
                request.getEmail())) {

            throw new RuntimeException(
                    "Email Already Registered");
        }

        boolean approved = true;

        if ("HIRING_DEPARTMENT".equals(
                request.getRole().name())) {

            approved = false;
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(
                        passwordEncoder.encode(
                                request.getPassword()))
                .phone(request.getPhone())
                .role(request.getRole())
                .status("ACTIVE")
                .approved(approved)
                .build();

        userRepository.save(user);

        if (!approved) {
            return "Registration Successful. Waiting for Admin Approval";
        }

        return "User Registered Successfully";
    }

    public AuthResponse login(
            LoginRequest request) {

        User user =
                userRepository.findByEmail(
                                request.getEmail())
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "User Not Found"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword())) {

            throw new RuntimeException(
                    "Invalid Credentials");
        }

        // Blocked Account Check
        if ("BLOCKED".equalsIgnoreCase(
                user.getStatus())) {

            throw new RuntimeException(
                    "Your account is blocked. Contact Admin.");
        }

        // Hiring Department Approval Check
        if ("HIRING_DEPARTMENT".equals(
                user.getRole().name())
                && (user.getApproved() == null
                        || !user.getApproved())) {

            throw new RuntimeException(
                    "Waiting For Admin Approval");
        }

        String token =
                jwtService.generateToken(
                        user.getEmail(),
                        user.getRole().name());

        return new AuthResponse(
                token,
                user.getRole().name());
    }
}