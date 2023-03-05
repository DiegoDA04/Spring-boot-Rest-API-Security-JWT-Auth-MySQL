package com.example.loginbackend.security.domain.service.communication.response;

import com.example.loginbackend.security.domain.service.communication.request.RegisterRequest;
import com.example.loginbackend.security.resource.UserResource;
import com.example.loginbackend.shared.domain.service.communication.BaseResponse;

public class RegisterResponse extends BaseResponse<UserResource> {
    public RegisterResponse(String message) {
        super(message);
    }

    public RegisterResponse(UserResource resource) {
        super(resource);
    }
}
