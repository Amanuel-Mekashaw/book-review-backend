package com.project.bookreviewapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {

    @JsonProperty("bookId")
    @NotNull
    private Long bookId;

    @JsonProperty("userId")
    @NotNull
    private Long userId;

    @JsonProperty("ratingValue")
    @NotNull
    private Integer ratingValue;

    @JsonProperty("comment")
    @Size(max = 255, message = "maximum of 255 character")
    private String comment;
}
