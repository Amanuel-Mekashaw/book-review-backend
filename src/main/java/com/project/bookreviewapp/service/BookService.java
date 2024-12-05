package com.project.bookreviewapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.bookreviewapp.entity.Book;

public interface BookService {
    // get all books
    Page<Book> getAllBooks(Pageable pageable);

    // get single book
    Book getBookById(Long id);

    // save book
    Book saveBook(Book book);

    // delete book
    void deleteBook(Long id) throws Exception;

    // check if the queried book exists
    boolean isBookExist(Book book);

    List<Book> getBookWithGenres(Long id);

    List<Book> getAllBooksWithGenres();
}
