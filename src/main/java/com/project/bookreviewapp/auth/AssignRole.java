package com.project.bookreviewapp.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignRole {
    @NotNull(message = "username cannot be blank")
    private String username;

    @NotNull(message = "role cannot be blank")
    private String role;

}
