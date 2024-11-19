package com.project.bookreviewapp.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorDetailDTO {
    // @NotNull(message = "User ID cannot be null")
    private Long id;

    @Size(max = 1000, message = "Biography must be less than or equal to 500 characters")
    private String biography;

    private String profilePicture;

    private Long userId;

    @Size(max = 5, message = "There can be a maximum of 5 social links")
    private List<@Pattern(regexp = SocialLinkValidator.URL_PATTERN, message = "Invalid URL format for social link") String> socialLinks;

    public static class SocialLinkValidator {
        // Constant for the URL pattern validation
        public static final String URL_PATTERN = "^(https?://)?(www\\.)?[a-zA-Z0-9-]+\\.[a-zA-Z]{2,}(\\/.*)?$";
    }

}