package com.project.bookreviewapp.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

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
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectionDTO {

    private Long id;

    @NotNull
    @Size(min = 3, message = "minimum 3 character")
    private String name;

    @Size(max = 150, message = "maximum of 150 character")
    private String description;

    @NotNull(message = "user id can't be null")
    private Long userId;

    @NotNull(message = "isPrivate can't be null")
    @JsonProperty("isPrivate")
    private boolean isPrivate;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

}
