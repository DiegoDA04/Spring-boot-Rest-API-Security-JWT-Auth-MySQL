package com.example.loginbackend.security.domain.service.communication.response;

import com.example.loginbackend.security.domain.service.communication.request.LoginRequest;
import com.example.loginbackend.security.resource.AuthenticateResource;
import com.example.loginbackend.shared.domain.service.communication.BaseResponse;

public class LoginResponse extends BaseResponse<AuthenticateResource> {
    public LoginResponse(String message) {
        super(message);
    }

    public LoginResponse(AuthenticateResource resource) {
        super(resource);
    }
}
