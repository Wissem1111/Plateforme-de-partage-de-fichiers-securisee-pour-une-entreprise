package com.intership.file.share.auth.service;


import com.intership.file.share.auth.request.SignUpRequest;
import com.intership.file.share.auth.request.SigninRequest;
import com.intership.file.share.auth.response.JwtAuthenticationResponse;

public interface AuthenticationService {
    JwtAuthenticationResponse signup(SignUpRequest request);

    JwtAuthenticationResponse signin(SigninRequest request);
}