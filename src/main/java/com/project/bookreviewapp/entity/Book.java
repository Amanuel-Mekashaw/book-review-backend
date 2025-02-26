package com.project.bookreviewapp.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    @JsonView(BookAuthorView.Detailed.class)
    // @JsonBackReference
    private User author;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_genre", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    @JsonView(BookAuthorView.Summary.class)
    private List<Genre> genres;

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "book_collection", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "collection_id"))
    @JsonBackReference
    private List<Collection> collections = new ArrayList<>();

    @Column(name = "cover_image")
    @JsonView(BookAuthorView.Summary.class)
    private String coverImage;

    // // TODO Make the document upload system
    // @Column(name = "document")
    // @JsonView(BookAuthorView.Summary.class)
    // private String document;

    public String getCoverImageUrl() {
        return "/api/v1/images/book_images/" + coverImage;
    }

    @OneToMany(mappedBy = "book", cascade = { CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH })
    @JsonIgnore
    private List<Rating> ratings; // List of ratings for the book

    @JsonView(BookAuthorView.Summary.class)
    @Transient // gets Calculated dynamically
    @Column(name = "average_rating")
    private double averageRating; // Calculated dynamically

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

    // calculate average rating dynamically
    public double getAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream().mapToInt(Rating::getRatingValue).average().orElse(0.0);
    }

    public void addCollection(Collection collection) {
        if (!collections.contains(collection)) {
            collections.add(collection);
            collection.getBooks().add(this);
        }
    }

    public void removeCollection(Collection collection) {
        if (collections.contains(collection)) {
            collections.remove(collection);
            collection.getBooks().remove(this);
        }
    }

    public void removeRating(Rating rating) {
        if (ratings.contains(rating)) {
            ratings.remove(rating);
            rating.setBook(null);
        }
    }

}
