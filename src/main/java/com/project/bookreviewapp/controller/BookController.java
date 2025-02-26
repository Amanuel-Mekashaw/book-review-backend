package com.project.bookreviewapp.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.bookreviewapp.dto.BookAuthorView;
import com.project.bookreviewapp.dto.BookDTO;
import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.Genre;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.entity.User.Status;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Page<Book>> listAllBooks(@PageableDefault(size = 30) Pageable pageable) {
        Page<Book> books = bookService.getAllBooks(pageable);
        return new ResponseEntity<Page<Book>>(books, HttpStatus.OK);
    }

    @Operation(summary = "Get single book", security = @SecurityRequirement(name = "bearerAuth"))
    @JsonView(BookAuthorView.Detailed.class)
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Long id) {
        return new ResponseEntity<Book>(bookService.getBookById(id), HttpStatus.OK);
    }

    @Operation(summary = "Get books by title", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/by-title")
    public ResponseEntity<?> getBookByTitle(@RequestParam(required = true) String title,
            @PageableDefault(size = 20) Pageable pageable) {
        if (title != null) {
            return new ResponseEntity<Page<Book>>(bookService.findBookByTitle(title, pageable), HttpStatus.OK);
        } else {
            Page<Book> books = bookService.getAllBooks(pageable);
            return new ResponseEntity<>(books, HttpStatus.OK);
        }

    }

    @Operation(summary = "Get books by language", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/by-language")
    public ResponseEntity<?> getBookByLanguage(@RequestParam(required = true) String language,
            @PageableDefault(size = 30) Pageable pageable) {
        ApiResponse<?> apiResponse;

        if (language != null) {
            List<Book> booksByLanguage = bookService.filterBooksByLanguage(language);
            apiResponse = new ApiResponse<>("Books by language found", 200, booksByLanguage);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        apiResponse = new ApiResponse<>("books by language not found", 404, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

    }

    @Operation(summary = "Get books by published year", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/by-publishedyear")
    public ResponseEntity<ApiResponse<List<Book>>> getBookByLanguage(
            @RequestParam(required = true) Integer publishedYear,
            @PageableDefault(size = 30) Pageable pageable) {
        ApiResponse<List<Book>> apiResponse;

        if (publishedYear != null) {
            List<Book> booksByLanguage = bookService.filterBooksByPublishedYear(publishedYear);
            apiResponse = new ApiResponse<>("Books by published year found", 200, booksByLanguage);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        apiResponse = new ApiResponse<>("books by published year not found", 404, null);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);

    }

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

    @Operation(summary = "Get all book by user id", security = @SecurityRequirement(name = "bearerAuth"))
    @JsonView(BookAuthorView.Detailed.class)
    @GetMapping("/book-by-author/{authorId}")
    public ResponseEntity<ApiResponse<?>> getBookByUserId(@PathVariable Long authorId) {
        List<Book> optionalBook = bookService.findBookByUserId(authorId);

        ApiResponse<?> apiResponse;
        // System.out.println("\n\n\n" + optionalBook + "\n\n\n");

        if (optionalBook != null) {
            apiResponse = new ApiResponse<>("All books found successfully", 200, optionalBook);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<>("Books with user id not found", 404, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        // ApiResponse<?> apiResponse;
        // if (optionalBook != null) {
        // apiResponse = new ApiResponse<List<Book>>("Found the books", 200,
        // optionalBook);
        // return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        // } else {
        // apiResponse = new ApiResponse<String>("Books not found by the author ", 404,
        // "");
        // return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        // }
    }

    // all books with genre
    @GetMapping("/allgenre")
    public ResponseEntity<List<Book>> getAllBooksWithGenres() {
        List<Book> books = bookService.getAllBooksWithGenres();
        return ResponseEntity.ok(books);
    }

    @Operation(summary = "Create book using book cover file", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/bycover")
    public ResponseEntity<ApiResponse<String>> addNewBook(@RequestParam("book") String bookDto,
            @RequestParam("coverImage") MultipartFile coverImage) throws JsonMappingException, JsonProcessingException {

        ApiResponse<String> apiResponse;

        BookDTO bookRequest = new ObjectMapper().readValue(bookDto, BookDTO.class);

        User foundAuthor = userRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("user by " + bookRequest.getAuthorId() + " not found"));

        System.out.println("\n\n\nstatus: " + foundAuthor.getStatus() + "\n\n\n");

        if (foundAuthor.getStatus() == Status.INACTIVE) {
            apiResponse = new ApiResponse<>("you can't update books admin have inactived your account", 200, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } else {
            System.out.println("\n\n\n" + bookRequest + "\n\n\n");
            bookService.addNewBook(bookRequest, coverImage);
            apiResponse = new ApiResponse<>("Book saved sucssessfully", 201, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        }

    }

    @Operation(summary = "Update book using book cover file", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/bycover/{id}")
    public ResponseEntity<ApiResponse<Book>> updateExistingBook(@PathVariable() Long id,
            @RequestParam("book") String bookDto, @RequestParam("coverImage") MultipartFile coverImage)
            throws JsonMappingException, JsonProcessingException {

        ApiResponse<Book> apiResponse;
        BookDTO bookRequest = new ObjectMapper().readValue(bookDto, BookDTO.class);

        // get book by id
        Book foundBook = bookService.getBookById(id);

        // get user by id
        User foundAuthor = userRepository.findById(bookRequest.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("user by " + bookRequest.getAuthorId() + " not found"));

        if (foundAuthor.getStatus() == Status.INACTIVE) {
            apiResponse = new ApiResponse<>("you can't update books admin have inactived your account", 200, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        // get the genres by id
        List<Genre> genres = genreRepository.findAllById(bookRequest.getGenreIds());
        if (genres.size() != bookRequest.getGenreIds().size()) {
            throw new EntityNotFoundException("One or more genres not found");
        }

        if (foundBook != null && foundAuthor != null) {
            foundBook = BookMapper.bookDtoToBook(bookRequest, userRepository, genreRepository);
            foundBook.setId(id);
            if (foundBook.getCreatedAt() == null) {
                foundBook.setCreatedAt(LocalDateTime.now());
            }
            foundBook.setCreatedAt(foundBook.getCreatedAt());
            foundBook.setGenres(genres);
            bookService.addNewBook(bookRequest, coverImage);

            apiResponse = new ApiResponse<Book>("book updated successfully", 200, foundBook);
            return new ResponseEntity<ApiResponse<Book>>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<Book>("book didn't update", 404, null);
            return new ResponseEntity<ApiResponse<Book>>(apiResponse, HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "create a book", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<ApiResponse<Book>> createBook(@RequestBody @Valid BookDTO bookDTO) {
        ApiResponse<Book> apiResponse;
        User foundAuthor = userRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("user by " + bookDTO.getAuthorId() + " not found"));

        if (foundAuthor.getStatus() == Status.INACTIVE) {
            apiResponse = new ApiResponse<>("you can't create books admin have inactived your account", 200, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

        // Fetch the genres
        List<Genre> genres = genreRepository.findAllById(bookDTO.getGenreIds());
        if (genres.size() != bookDTO.getGenreIds().size()) {
            throw new EntityNotFoundException("One or more genres not found");
        }

        System.err.println("\n\n\n " + foundAuthor + "\n\n\n");

        Book createdBook = BookMapper.bookDtoToBook(bookDTO, userRepository, genreRepository);
        createdBook.setGenres(genres);

        createdBook = bookService.saveBook(createdBook);

        apiResponse = new ApiResponse<Book>("Book saved successfully", 201, createdBook);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @Operation(summary = "update a book", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Book>> updateBook(@RequestBody @Valid BookDTO bookDTO, @PathVariable Long id) {
        ApiResponse<Book> apiResponse;
        Book foundBook = bookService.getBookById(id);

        User foundAuthor = userRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException("user by " + bookDTO.getAuthorId() + " not found"));

        if (foundAuthor.getStatus() == Status.INACTIVE) {
            apiResponse = new ApiResponse<>("you can't update books admin have inactived your account", 200, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }
        // Fetch the genres
        List<Genre> genres = genreRepository.findAllById(bookDTO.getGenreIds());
        if (genres.size() != bookDTO.getGenreIds().size()) {
            throw new EntityNotFoundException("One or more genres not found");
        }

        if (foundBook != null && foundAuthor != null) {
            foundBook = BookMapper.bookDtoToBook(bookDTO, userRepository, genreRepository);
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

    @Operation(summary = "update rating of a book", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/rating/{id}")
    public ResponseEntity<ApiResponse<String>> updateBookRating(@RequestBody @Valid Long rating,
            @PathVariable Long id) {
        Book foundBook = bookService.getBookById(id);
        ApiResponse<String> apiResponse;
        if (foundBook != null) {
            foundBook.setAverageRating(rating);
            foundBook = bookService.saveBook(foundBook);
            apiResponse = new ApiResponse<String>("Rating Updated successfully", 200, "");
            return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<String>("Rating didn't Updated ", 401, "");
            return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
        }
    }

    @Operation(summary = "delete a book", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteBook(@PathVariable Long id) throws Exception {
        bookService.deleteBook(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Deleted book with isbn " + id, 404);

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}
