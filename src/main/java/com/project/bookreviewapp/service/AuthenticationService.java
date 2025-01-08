package com.project.bookreviewapp.service;

import java.util.List;

import com.project.bookreviewapp.auth.AuthenticationRequest;
import com.project.bookreviewapp.auth.AuthenticationResponse;
import com.project.bookreviewapp.auth.RegisterRequest;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.entity.User.Status;

public interface AuthenticationService {

    public User findUserById(Long id);

    public AuthenticationResponse register(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception;

    public void assignRole(String token, String email, String role) throws Exception;

    public void assignStatus(String token, String email, Status state) throws Exception;

    public List<User> getAllUsers(String token) throws Exception;

    void deleteUserById(Long id, String token) throws Exception;

}
