package com.project.bookreviewapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AppealDTO {
    @Email
    @NotBlank
    @JsonProperty("email")
    String email;

    @NotBlank
    @Size(min = 5, message = "reason connot be less than 5 character")
    @Size(max = 1000, message = "reason connot be greater than 1000 character")
    @JsonProperty("message")
    String message;
}