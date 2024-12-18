package com.project.bookreviewapp.service.implementation;

import org.springframework.stereotype.Service;

import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.Rating;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.repository.RatingRepository;
import com.project.bookreviewapp.service.RatingService;

@Service
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;

    public RatingServiceImpl(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    @Override
    public void addRating(User user, Book book, int ratingValue) {
        Rating existingRating = ratingRepository.findByUserAndBook(user, book).orElse(null);

        if (existingRating != null) {
            // Update existing rating if user already rated the book
            existingRating.setRatingValue(ratingValue);
            ratingRepository.save(existingRating);
        } else {
            // Add new rating if the user hasn't rated the book yet
            Rating newRating = new Rating();
            newRating.setUser(user);
            newRating.setBook(book);
            newRating.setRatingValue(ratingValue);
            ratingRepository.save(newRating);
        }
    }

}
