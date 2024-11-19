package com.project.bookreviewapp.auth;

import com.project.bookreviewapp.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private User user;

    AuthenticationResponse(String token) {
        this.token = token;
    }
}
