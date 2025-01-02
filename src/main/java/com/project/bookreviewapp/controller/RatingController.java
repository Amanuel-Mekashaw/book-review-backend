package com.project.bookreviewapp.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.dto.RatingDTO;
import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.Rating;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.repository.BookRepository;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.RatingService;
import com.project.bookreviewapp.utils.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/bookrating")
@RequiredArgsConstructor
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    @GetMapping("/getratings/{id}")
    public ResponseEntity<ApiResponse<List<Rating>>> getAllRatingByBook(@PathVariable Long id) {
        ApiResponse<List<Rating>> apiResponse;

        Book book = bookRepository.findById(id).orElse(null);

        if (book != null) {
            List<Rating> ratings = ratingService.getAllRatingsByBook(book);
            apiResponse = new ApiResponse<>("Rating found", 200, ratings);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<>("Rating not found", 404, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/rate")
    public ResponseEntity<ApiResponse<String>> rateBook(@RequestParam(required = true) Long userId,
            @RequestParam(required = true) Long bookId, @RequestParam() int ratingValue) {

        ApiResponse<String> apiResponse;

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (user != null && book != null) {
            apiResponse = new ApiResponse<>("Rating submitted successfully", 200, null);
            ratingService.addRating(user, book, ratingValue);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<>("Rating failed", 401, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }

    @PostMapping("/ratecomment")
    public ResponseEntity<ApiResponse<String>> rateAndCommentOnBook(@RequestBody @Valid RatingDTO ratingDTO) {
        System.out.println("\n\n\n " + ratingDTO + "\n\n\n");

        ApiResponse<String> apiResponse;

        User user = userRepository.findById(ratingDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Book book = bookRepository.findById(ratingDTO.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (user != null && book != null) {
            apiResponse = new ApiResponse<>("Rating submitted successfully", 200);
            ratingService.addRatingAndComment(user, book, ratingDTO.getRatingValue(), ratingDTO.getComment());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<>("Rating failed", 401);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }

    @GetMapping("/getratingsbyvalue")
    public ResponseEntity<ApiResponse<List<Rating>>> searchRatings(@RequestParam Long bookId,
            @RequestParam int ratingValue) {

        ApiResponse<List<Rating>> apiResponse;

        List<Rating> ratingsByRatingValue = ratingService.getRatingsByBookIdAndValue(bookId, ratingValue);
        if (ratingsByRatingValue != null) {
            apiResponse = new ApiResponse<>("Rating found", 200, ratingsByRatingValue);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<>("Rating not found", 404, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

        }

    }
}
