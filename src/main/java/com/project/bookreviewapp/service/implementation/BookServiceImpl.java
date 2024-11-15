package com.project.bookreviewapp.service.implementation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.repository.BookRepository;
import com.project.bookreviewapp.service.BookService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<Book> getAllBooks(Pageable pageable) {
        Page<Book> books = bookRepository.findAll(pageable);
        return books;
    }

    @Override
    public Book getBookById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("book with " + id + "not found"));
    }

    @Override
    public Book saveBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public void deleteBook(Long id) throws Exception {
        try {
            bookRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            log.debug("book with " + id + " doesn't exist" + ex.getMessage());
        }
    }

    @Override
    public boolean isBookExist(Book book) {
        return bookRepository.existsById(book.getId());
    }

}
