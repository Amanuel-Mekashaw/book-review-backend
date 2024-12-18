package com.project.bookreviewapp.service;

import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.User;

public interface RatingService {
    public void addRating(User user, Book book, int ratingValue);

    public void addRatingAndComment(User user, Book book, int ratingValue, String comment);
}
