package com.example.loginbackend.security.service;

import com.example.loginbackend.security.domain.model.entity.Role;
import com.example.loginbackend.security.domain.model.entity.User;
import com.example.loginbackend.security.domain.model.enumeration.Roles;
import com.example.loginbackend.security.domain.persistence.RoleRepository;
import com.example.loginbackend.security.domain.persistence.UserRepository;
import com.example.loginbackend.security.domain.service.UserService;
import com.example.loginbackend.security.domain.service.communication.request.LoginRequest;
import com.example.loginbackend.security.domain.service.communication.request.RegisterRequest;
import com.example.loginbackend.security.domain.service.communication.response.LoginResponse;
import com.example.loginbackend.security.domain.service.communication.response.RegisterResponse;
import com.example.loginbackend.security.middleware.JwtHandler;
import com.example.loginbackend.security.middleware.UserDetailsImpl;
import com.example.loginbackend.security.resource.AuthenticateResource;
import com.example.loginbackend.security.resource.UserResource;
import com.example.loginbackend.shared.mapping.EnhancedModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private  RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private  JwtHandler jwtHandler;
    @Autowired
    private  PasswordEncoder encoder;
    @Autowired
    private  EnhancedModelMapper mapper;

    @Override
    public ResponseEntity<?> authenticate(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();

            String token = jwtHandler.generateTokenFromUsername(username);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());
            AuthenticateResource resource = mapper.map(userDetails, AuthenticateResource.class);
            resource.setToken(token);
            resource.setRoles(roles);
            LoginResponse response = new LoginResponse(resource);
            return ResponseEntity.ok(response.getResource());
        } catch (Exception e) {
            LoginResponse response = new LoginResponse(
                    String.format("An error occurred while authenticating: %s", e.getMessage()));
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> register(RegisterRequest request) {
        if(userRepository.existsByUsername(request.getUsername())) {
            RegisterResponse response = new RegisterResponse("Username is already used.");
            return ResponseEntity.badRequest().body(response.getMessage());
        }

        if(userRepository.existsByUsername(request.getEmail())) {
            RegisterResponse response = new RegisterResponse("Email is already used.");
            return ResponseEntity.badRequest().body(response.getMessage());
        }

        try {
            Set<String> rolesStringSet = request.getRoles();
            Set<Role> roles = new HashSet<>();

            if(rolesStringSet == null) {
                roleRepository.findByName(Roles.ROLE_USER)
                        .map(roles::add)
                        .orElseThrow(() -> new RuntimeException("Role not found"));
            } else {
                rolesStringSet.forEach(roleString ->
                        roleRepository.findByName(Roles.valueOf(roleString))
                                .map(roles::add)
                                .orElseThrow(() -> new RuntimeException("Role not found")));
            }

            User user = new User()
                    .withUsername(request.getUsername())
                    .withEmail(request.getEmail())
                    .withPassword(encoder.encode(request.getPassword()))
                    .withRoles(roles);

            userRepository.save(user);
            UserResource userResource = mapper.map(user, UserResource.class);
            RegisterResponse response = new RegisterResponse(userResource);

            return ResponseEntity.ok(response.getResource());
        } catch (Exception e) {
            RegisterResponse response = new RegisterResponse(e.getMessage());
            return ResponseEntity.badRequest().body(response.getMessage());
        }
    }

    @Override
    public ResponseEntity<?> logout() {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User not found with username: %s", username)));

        return UserDetailsImpl.build(user);
    }
}
