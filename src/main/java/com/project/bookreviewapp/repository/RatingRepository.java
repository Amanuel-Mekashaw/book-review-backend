package com.project.bookreviewapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.Rating;
import com.project.bookreviewapp.entity.User;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findByBook(Book book);

    List<Rating> findByBookIdAndRatingValue(Long bookId, int ratingValue);

    Optional<Rating> findByUserAndBook(User user, Book book);

}
