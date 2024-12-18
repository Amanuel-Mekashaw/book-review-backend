package com.project.bookreviewapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.dto.RatingDTO;
import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.repository.BookRepository;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.RatingService;
import com.project.bookreviewapp.utils.ApiResponse;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/bookrating")
public class RatingController {

    private final RatingService ratingService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public RatingController(RatingService ratingService, UserRepository userRepository, BookRepository bookRepository) {
        this.ratingService = ratingService;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
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
    public ResponseEntity<ApiResponse<String>> rateAndCommentOnBook(@RequestBody() @Valid RatingDTO ratingDTO) {

        ApiResponse<String> apiResponse;

        User user = userRepository.findById(ratingDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Book book = bookRepository.findById(ratingDTO.getBookId())
                .orElseThrow(() -> new EntityNotFoundException("Book not found"));

        if (user != null && book != null) {
            apiResponse = new ApiResponse<>("Rating submitted successfully", 200, null);
            ratingService.addRatingAndComment(user, book, ratingDTO.getRatingValue(), ratingDTO.getComment());
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<>("Rating failed", 401, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
    }
}
