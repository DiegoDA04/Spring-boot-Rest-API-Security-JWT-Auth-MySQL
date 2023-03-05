package com.example.loginbackend.security.domain.service;

import com.example.loginbackend.security.domain.service.communication.request.LoginRequest;
import com.example.loginbackend.security.domain.service.communication.request.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    ResponseEntity<?> authenticate(LoginRequest request);

    ResponseEntity<?> register(RegisterRequest request);

    ResponseEntity<?> logout();
}
