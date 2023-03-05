package com.example.loginbackend.security.resource;

import com.example.loginbackend.security.domain.model.entity.Role;
import com.example.loginbackend.security.domain.model.enumeration.Roles;
import com.example.loginbackend.security.domain.persistence.RoleRepository;
import com.example.loginbackend.security.domain.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void seed() {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role().withName(role));
            }
        });
    }
}
