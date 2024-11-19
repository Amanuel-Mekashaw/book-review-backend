package com.project.bookreviewapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    @NotBlank
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String userName;

    @NotBlank
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank
    @Size(min = 6, message = "Password hash must be at least 6 characters long")
    private String passwordHash;

    @NotBlank
    private String role; // e.g., {"USER", "AUTHOR", "ADMIN"}

    @NotBlank
    private String status; // e.g., {"ACTIVE", "INACTIVE"}

    // Optional: only include these fields if you want them in the creation process
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
