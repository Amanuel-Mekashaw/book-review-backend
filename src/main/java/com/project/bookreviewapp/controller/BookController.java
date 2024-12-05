package com.project.bookreviewapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.project.bookreviewapp.dto.BookAuthorView;
import com.project.bookreviewapp.dto.BookDTO;
import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.Genre;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.mapper.BookMapper;
import com.project.bookreviewapp.repository.GenreRepository;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.BookService;
import com.project.bookreviewapp.utils.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private final BookService bookService;
    public final UserRepository userRepository;
    public final GenreRepository genreRepository;

    public BookController(BookService bookService, UserRepository userRepository, GenreRepository genreRepository) {
        this.bookService = bookService;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @Operation(summary = "Get all books", security = @SecurityRequirement(name = "bearerAuth"))
    @JsonView(BookAuthorView.Summary.class)
    @GetMapping
    public ResponseEntity<Page<Book>> listAllBooks(@PageableDefault(size = 10) Pageable pageable) {
        Page<Book> books = bookService.getAllBooks(pageable);
        return new ResponseEntity<Page<Book>>(books, HttpStatus.OK);
    }

    @Operation(summary = "Get single book", security = @SecurityRequirement(name = "bearerAuth"))
    @JsonView(BookAuthorView.Detailed.class)
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return new ResponseEntity<Book>(bookService.getBookById(id), HttpStatus.OK);
    }

    // all books with genre
    @Operation(summary = "Get single book", security = @SecurityRequirement(name = "bearerAuth"))
    @JsonView(BookAuthorView.Detailed.class)
    @GetMapping("/genre/{id}")
    public ResponseEntity<?> getBookWithGenres(@PathVariable Long id) {
        List<Book> optionalBook = bookService.getBookWithGenres(id);

        if (optionalBook != null) {
            return ResponseEntity.ok(optionalBook);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Book not found", "id", id));
        }
    }

    @GetMapping("/allgenre")
    public ResponseEntity<List<Book>> getAllBooksWithGenres() {
        List<Book> books = bookService.getAllBooksWithGenres();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "create a book", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createBook(@RequestBody @Valid BookDTO bookDTO) {
        User foundAuthor = userRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("user by " + bookDTO.getAuthorId() + " not found"));

        // Fetch the genres
        List<Genre> genres = genreRepository.findAllById(bookDTO.getGenreIds());
        if (genres.size() != bookDTO.getGenreIds().size()) {
            throw new EntityNotFoundException("One or more genres not found");
        }

        System.err.println("\n\n\n " + foundAuthor + "\n\n\n");

        Book createdBook = BookMapper.bookDtoToBook(bookDTO, userRepository);
        // createdBook.setAuthor(foundAuthor);
        createdBook.setGenres(genres);

        createdBook = bookService.saveBook(createdBook);

        ApiResponse<Book> apiResponse = new ApiResponse<Book>("Book saved successfully", 201, createdBook);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "update a book", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@RequestBody @Valid BookDTO bookDTO, @PathVariable Long id) {
        Book foundBook = bookService.getBookById(id);

        User foundAuthor = userRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("user by " + bookDTO.getAuthorId() + " not found"));
        // Fetch the genres
        List<Genre> genres = genreRepository.findAllById(bookDTO.getGenreIds());
        if (genres.size() != bookDTO.getGenreIds().size()) {
            throw new EntityNotFoundException("One or more genres not found");
        }

        ApiResponse<Book> apiResponse;
        if (foundBook != null && foundAuthor != null) {
            foundBook = BookMapper.bookDtoToBook(bookDTO, userRepository);
            foundBook.setId(id);
            if (foundBook.getCreatedAt() == null) {
                foundBook.setCreatedAt(LocalDateTime.now());
            }
            foundBook.setCreatedAt(foundBook.getCreatedAt());
            foundBook.setGenres(genres);
            foundBook = bookService.saveBook(foundBook);

            apiResponse = new ApiResponse<Book>("book updated successfully", 200, foundBook);
            return new ResponseEntity<ApiResponse<Book>>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<Book>("book didn't update", 401, null);
            return new ResponseEntity<ApiResponse<Book>>(apiResponse, HttpStatus.OK);
        }
    }

    @Operation(summary = "delete a book", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) throws Exception {
        bookService.deleteBook(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Deleted book with isbn " + id, 404);

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }
}
