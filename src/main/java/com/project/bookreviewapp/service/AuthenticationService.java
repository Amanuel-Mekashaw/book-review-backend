package com.project.bookreviewapp.service;

import com.project.bookreviewapp.auth.AuthenticationRequest;
import com.project.bookreviewapp.auth.AuthenticationResponse;
import com.project.bookreviewapp.auth.RegisterRequest;

public interface AuthenticationService {

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception;
}
