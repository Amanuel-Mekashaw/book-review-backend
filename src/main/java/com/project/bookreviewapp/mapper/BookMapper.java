package com.project.bookreviewapp.mapper;

import com.project.bookreviewapp.dto.BookDTO;
import com.project.bookreviewapp.entity.Book;

public class BookMapper {

    public static BookDTO bookToBookDto(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .description(book.getDescription())
                .publishedYear(book.getPublishedYear())
                .publisher(book.getPublisher())
                .pages(book.getPages())
                .language(book.getLanguage())
                .coverImage(book.getCoverImage())
                .createdAt(book.getCreatedAt())
                .updatedAt(book.getUpdatedAt())
                .build();
    }

    public static Book bookDtoToBook(BookDTO bookDTO) {
        return Book.builder()
                .id(bookDTO.getId())
                .title(bookDTO.getTitle())
                .isbn(bookDTO.getIsbn())
                .description(bookDTO.getDescription())
                .publishedYear(bookDTO.getPublishedYear())
                .publisher(bookDTO.getPublisher())
                .pages(bookDTO.getPages())
                .language(bookDTO.getLanguage())
                .coverImage(bookDTO.getCoverImage())
                .createdAt(bookDTO.getCreatedAt())
                .updatedAt(bookDTO.getUpdatedAt())
                .build();
    }
}
