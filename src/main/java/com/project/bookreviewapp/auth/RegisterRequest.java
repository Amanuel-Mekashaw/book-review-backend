package com.project.bookreviewapp.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    @NotNull
    private String username;

    @NotNull
    @Email(message = "invalid email structure")
    private String email;

    @NotNull
    @Size(min = 8, message = "password must be 8 character long")
    private String password;
}
