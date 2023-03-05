package com.example.loginbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LoginBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LoginBackendApplication.class, args);
    }

}
