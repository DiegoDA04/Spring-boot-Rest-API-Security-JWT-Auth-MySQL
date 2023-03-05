package com.example.loginbackend.security.resource;

import java.util.List;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class AuthenticateResource {
    private Long id;
    private String username;
    private String email;
    private List<String> roles;
    private String token;
}