package com.project.bookreviewapp.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonView;
import com.project.bookreviewapp.dto.BookAuthorView;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(BookAuthorView.Summary.class)
    private Long id;

    @Column(nullable = false)
    @JsonView(BookAuthorView.Summary.class)
    private String title;

    @JsonView(BookAuthorView.Summary.class)
    private String isbn;

    @JsonView(BookAuthorView.Summary.class)
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "published_year")
    @JsonView(BookAuthorView.Summary.class)
    private int publishedYear;

    @JsonView(BookAuthorView.Summary.class)
    private String publisher;

    @JsonView(BookAuthorView.Summary.class)
    private int pages;

    @JsonView(BookAuthorView.Summary.class)
    private String language;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    @JsonView(BookAuthorView.Detailed.class)
    @JsonBackReference
    private User author;

    // @JsonView(BookAuthorView.Detailed.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_genre", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonManagedReference
    @JsonView(BookAuthorView.Detailed.class)
    private List<Genre> genres;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(name = "book_collection", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "collection_id"))
    @JsonIgnore
    @JsonView(BookAuthorView.Summary.class)
    private List<Collection> collections;

    @Column(name = "cover_image")
    @JsonView(BookAuthorView.Summary.class)
    private String coverImage;

    @Column(name = "average_rating")
    @JsonView(BookAuthorView.Summary.class)
    private double averageRating;

    @Column(name = "rating_count")
    @JsonView(BookAuthorView.Summary.class)
    private int ratingCount;

    @CreatedDate
    @Column(name = "created_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonView(BookAuthorView.Summary.class)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @JsonView(BookAuthorView.Summary.class)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
