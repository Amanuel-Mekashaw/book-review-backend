package com.project.bookreviewapp.service.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.project.bookreviewapp.dto.BookDTO;
import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.mapper.BookMapper;
import com.project.bookreviewapp.repository.BookRepository;
import com.project.bookreviewapp.repository.GenreRepository;
import com.project.bookreviewapp.repository.RatingRepository;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.BookService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private UserRepository userRepository;
    private GenreRepository genreRepository;
    private RatingRepository ratingRepository;

    private static final String STORAGE_DIRECTORY = "D:\\test\\book_images";

    private static final long MAX_FILE_SIZE_MB = 4 * 1024 * 1024; // 4MB

    public BookServiceImpl(BookRepository bookRepository, UserRepository userRepository,
            GenreRepository genreRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
    }

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        // Append the storage path to the coverImage field for each book
        // books.forEach(book -> {
        // if (book.getCoverImage() != null) {
        // book.setCoverImage(STORAGE_DIRECTORY + File.separator +
        // book.getCoverImage());
        // }
        // });
        return books;
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findByIdWithGenres(id)
                .orElseThrow(() -> new EntityNotFoundException("book with " + id + " not found"));
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) throws Exception {
        try {
            Book book = bookRepository.findById(id).orElse(null);

            if (book != null) {
                bookRepository.deleteById(id);
            }

        } catch (EmptyResultDataAccessException ex) {
            log.debug("book with " + id + " doesn't exist" + ex.getMessage());
        }
    }

    @Override
    public boolean isBookExist(Book book) {
        return bookRepository.existsById(book.getId());
    }

    public List<Book> getBookWithGenres(Long id) {
        return bookRepository.findBooksByGenreId(id);
    }

    public List<Book> getAllBooksWithGenres() {
        return bookRepository.findAllWithGenres();
    }

    public Page<Book> findBookByTitle(String title, Pageable pageable) {
        return bookRepository.findByTitleContainingIgnoreCase(title, pageable);
    }

    @Override
    public List<Book> findBookByUserId(Long authorId) {
        return bookRepository.findBooksByUserId(authorId);
    }

    @Override
    public List<Book> findBooksByAuthorFirstNameOrLastName(String firstName, String lastName) {
        return bookRepository.findBooksByAuthorFirstNameOrLastName(firstName, lastName);
    }

    @Override
    public void addNewBook(BookDTO bookDTO, MultipartFile coverImage) throws RuntimeException {
        // Map BookRequest to Book entity
        try {

            Book book = BookMapper.bookDtoToBook(bookDTO, userRepository, genreRepository);
            // System.out.println("Book\n\n\n" + book);
            saveCoverImage(book, coverImage);
            bookRepository.save(book);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void saveCoverImage(Book book, MultipartFile coverImage) throws IOException {
        if (coverImage == null || coverImage.isEmpty()) {
            throw new IllegalArgumentException("Cover image is missing or empty");
        }

        if (coverImage.getSize() > MAX_FILE_SIZE_MB) {
            throw new IllegalArgumentException("File size exceeds the 4MB limit");
        }

        String originalFilename = coverImage.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Cover image filename is missing");
        }

        String extension = FilenameUtils.getExtension(originalFilename);
        System.out.println("\n\n\nExension" + extension);

        String filename = UUID.randomUUID().toString() + "." + extension;

        Path filePath = Paths.get(STORAGE_DIRECTORY, filename);
        Files.createDirectories(filePath.getParent());
        Files.copy(coverImage.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        book.setCoverImage(filename);
    }

    public File getCoverImage(String filename) {
        var file = new File(STORAGE_DIRECTORY + File.separator + filename);
        if (!file.exists() || !file.getParent().equals(STORAGE_DIRECTORY)) {
            throw new SecurityException("File not found or unsupported filename!");
        }
        return file;
    }

}
