package com.project.bookreviewapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    // @NotNull // it's auto generated
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

    private String profilePicture;

    @Size(max = 500, message = "Bio should not exceed 500 characters")
    private String bio;

    // Optional: only include these fields if you want them in the creation process
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
