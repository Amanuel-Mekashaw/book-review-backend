package com.project.bookreviewapp.controller;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.auth.AssignRole;
import com.project.bookreviewapp.auth.AuthenticationRequest;
import com.project.bookreviewapp.auth.AuthenticationResponse;
import com.project.bookreviewapp.auth.RegisterRequest;
import com.project.bookreviewapp.service.implementation.AuthenticationServiceImpl;
import com.project.bookreviewapp.utils.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@RequestBody @Valid RegisterRequest request) {

        ApiResponse<AuthenticationResponse> apiResponse = new ApiResponse<>("Authenticated Successfully", 200,
                authenticationService.register(request));
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> authenticate(
            @RequestBody @Valid AuthenticationRequest request) throws Exception {

        System.out.println("\n\n\n" + request + "\n\n\n");
        ApiResponse<AuthenticationResponse> apiResponse;
        try {
            apiResponse = new ApiResponse<>("successfull", 200, authenticationService.authenticate(request));
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } catch (AuthenticationException e) {
            apiResponse = new ApiResponse<>("unauthorized access invalid credientials", 401, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            apiResponse = new ApiResponse<>("error was thrown server error", 500, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/assign-role")
    public ResponseEntity<ApiResponse<String>> assignRole(@RequestHeader("Authorization") String token,
            @RequestBody @Valid AssignRole assignRole) throws Exception {

        ApiResponse<String> apiResponse;

        if (token == null || !token.startsWith("Bearer ")) {
            apiResponse = new ApiResponse<>("Token is missing or invalid", 400);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        try {
            authenticationService.assignRole(token, assignRole.getUsername(), assignRole.getRole());
        } catch (AuthenticationException e) {

            apiResponse = new ApiResponse<>(e.getMessage(), 403);
            return new ResponseEntity<>(apiResponse, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            apiResponse = new ApiResponse<>("An error occurred on the server", 500);
            return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        apiResponse = new ApiResponse<>("Role assigned successfully!", 200, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

}
