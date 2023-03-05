package com.example.loginbackend.security.resource;

import com.example.loginbackend.security.domain.model.entity.Role;
import lombok.*;

import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@With
public class UserResource {
    private Long id;
    private String username;
    private String email;
    private Set<Role> roles;
}
