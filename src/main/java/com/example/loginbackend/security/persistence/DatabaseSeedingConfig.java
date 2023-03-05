package com.example.loginbackend.security.persistence;

import com.example.loginbackend.security.domain.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DatabaseSeedingConfig {

    private final RoleService roleService;

    public DatabaseSeedingConfig(RoleService roleService) {
        this.roleService = roleService;
    }

    @EventListener
    public void onApplicationReady(ApplicationReadyEvent event) {
        roleService.seed();
    }
}
