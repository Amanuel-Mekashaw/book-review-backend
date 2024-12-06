package com.project.bookreviewapp.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

import com.project.bookreviewapp.dto.BookDTO;
import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.Genre;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.repository.CollectionRepository;
import com.project.bookreviewapp.repository.GenreRepository;
import com.project.bookreviewapp.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;

public class BookMapper {

    public static BookDTO bookToBookDto(Book book) {
        BookDTO bookDTO = new BookDTO();
        BeanUtils.copyProperties(book, bookDTO);

        if (book.getAuthor().getId() != 0) {
            Long userId = book.getAuthor().getId();
            bookDTO.setAuthorId(userId);

        }

        // Map genres to genreIds
        if (book.getGenres() != null) {
            List<Long> genreIds = book.getGenres().stream().map(Genre::getId).collect(Collectors.toList());
            bookDTO.setGenreIds(genreIds);
        }

        return bookDTO;

    }

    public static Book bookDtoToBook(BookDTO bookDTO, UserRepository userRepository, GenreRepository genreRepository) {
        Book book = new Book();
        BeanUtils.copyProperties(bookDTO, book);

        if (bookDTO.getAuthorId() != null && bookDTO.getAuthorId() != 0) {
            // Fetch the full User object from the repository
            User user = userRepository.findById(bookDTO.getAuthorId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found"));
            book.setAuthor(user);
        }

        // Map genreIds to Genres
        if (bookDTO.getGenreIds() != null && !bookDTO.getGenreIds().isEmpty()) {
            List<Genre> genres = genreRepository.findAllById(bookDTO.getGenreIds());
            book.setGenres(genres);
        }

        return book;
    }

    // public static Book bookDtoToBook(BookDTO bookDTO) {
    // Book book = new Book();
    // BeanUtils.copyProperties(bookDTO, book);

    // if (bookDTO.getAuthorId() != 0) {
    // User user = new User();
    // user.setId(bookDTO.getAuthorId());
    // book.setAuthor(user);
    // }

    // return book;

    // // return
    // //
    // Book.builder().id(bookDTO.getId()).title(bookDTO.getTitle()).isbn(bookDTO.getIsbn())
    // //
    // .description(bookDTO.getDescription()).publishedYear(bookDTO.getPublishedYear())
    // //
    // .publisher(bookDTO.getPublisher()).pages(bookDTO.getPages()).language(bookDTO.getLanguage())
    // //
    // .coverImage(bookDTO.getCoverImage()).createdAt(bookDTO.getCreatedAt()).updatedAt(bookDTO.getUpdatedAt())
    // // .build();
    // }
}
