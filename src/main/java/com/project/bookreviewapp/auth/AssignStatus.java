package com.project.bookreviewapp.auth;

import com.project.bookreviewapp.entity.User.Status;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AssignStatus {
    @NotNull(message = "email cannot be blank")
    private String email;

    @NotNull(message = "status cannot be blank")
    private Status status;

}
