package com.project.bookreviewapp.dto;

import io.micrometer.common.lang.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    @NotNull(message = "bookId cannot be null")
    @NotBlank
    private Long bookId;

    @NotNull(message = "userId cannot be null")
    @NotBlank
    private Long userId;

    @NotNull(message = "rating value cannot be null")
    @NotBlank
    private int ratingValue;

    @Size(max = 255, message = "maximum of 255 character")
    private String comment;

}
