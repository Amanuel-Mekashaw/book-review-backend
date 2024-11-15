package com.project.bookreviewapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.dto.BookDTO;
import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.mapper.BookMapper;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.BookService;
import com.project.bookreviewapp.utils.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    public final UserRepository userRepository;

    public BookController(BookService bookService, UserRepository userRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Book>> listAllBooks(@PageableDefault(size = 10) Pageable pageable) {
        Page<Book> books = bookService.getAllBooks(pageable);
        return new ResponseEntity<Page<Book>>(books, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return new ResponseEntity<Book>(bookService.getBookById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createBook(@RequestBody @Valid BookDTO bookDTO) {
        System.err.println("\n\n\n " + bookDTO + "\n\n\n");
        User foundAuthor = userRepository.findById(bookDTO.getAuthorId()).orElseThrow(
                () -> new EntityNotFoundException("user by " + bookDTO.getAuthorId() + " not found"));

        System.err.println("\n\n\n " + foundAuthor + "\n\n\n");

        Book createdBook = BookMapper.bookDtoToBook(bookDTO);
        createdBook.setAuthor(foundAuthor);
        createdBook.setCreatedAt(LocalDateTime.now());
        createdBook.setUpdatedAt(LocalDateTime.now());

        createdBook = bookService.saveBook(createdBook);

        ApiResponse<Book> apiResponse = new ApiResponse<Book>("Book saved successfully", 201, createdBook);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@RequestBody @Valid BookDTO bookDTO, @PathVariable Long id) {
        Book foundBook = bookService.getBookById(id);

        User foundAuthor = userRepository.findById(bookDTO.getAuthorId()).orElseThrow(
                () -> new EntityNotFoundException("user by " + bookDTO.getAuthorId() + " not found"));

        foundBook = BookMapper.bookDtoToBook(bookDTO);
        foundBook.setAuthor(foundAuthor);
        foundBook = bookService.saveBook(foundBook);

        ApiResponse<Book> apiResponse = new ApiResponse<Book>("book updated successfully", 200, foundBook);

        return new ResponseEntity<ApiResponse<Book>>(apiResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) throws Exception {
        bookService.deleteBook(id);
        ApiResponse<String> apiResponse = new ApiResponse<>(
                "Deleted book with isbn " + id, 404);

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}
