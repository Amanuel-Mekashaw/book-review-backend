package com.project.bookreviewapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class BookDTO {

    private Long id;
    @NotNull(message = "Book title cannot be null")
    private String title;
    @NotNull(message = "Book isbn cannot be null")
    private String isbn;

    @NotBlank(message = "provide atleast a brief details about the book")
    @Size(max = 1000, message = "Description can't be greater than 700 letter")
    private String description;

    @NotNull(message = "Author id cannot be null")
    private Long authorId;

    @NotNull(message = "Book published year cannot be null")
    private int publishedYear;
    private String publisher;
    private int pages;
    private String language;

    private List<Long> genreIds; // List of genre IDs

    private String coverImage;
    // private String document;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // private LocalDateTime createdAt;

    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    // private LocalDateTime updatedAt;
}
