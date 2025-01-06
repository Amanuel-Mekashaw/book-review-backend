package com.project.bookreviewapp.service;

import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.Rating;
import com.project.bookreviewapp.entity.User;

import java.util.List;

public interface RatingService {
    public void addRating(User user, Book book, int ratingValue);

    public void addRatingAndComment(User user, Book book, int ratingValue, String comment);

    List<Rating> getAllRatingsByBook(Book book);

    public List<Rating> getRatingsByBookIdAndValue(Long bookId, int ratingValue);

    public List<Rating> getRatingsByUserId(Long userId);
}
