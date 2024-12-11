package com.project.bookreviewapp.service.implementation;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.bookreviewapp.entity.Book;
import com.project.bookreviewapp.entity.Collection;
import com.project.bookreviewapp.repository.BookRepository;
import com.project.bookreviewapp.repository.CollectionRepository;
import com.project.bookreviewapp.service.CollectionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CollectionServiceImpl implements CollectionService {

    private final CollectionRepository collectionRepository;
    private final BookRepository bookRepository;

    public CollectionServiceImpl(CollectionRepository collectionRepository, BookRepository bookRepository) {
        this.collectionRepository = collectionRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public Page<Collection> getAllCollection(Pageable pageable) {
        return collectionRepository.findAll(pageable);
    }

    @Override
    public Collection getCollection(Long id) {
        return collectionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Collection by this id not found"));
    }

    @Override
    public Collection saveCollection(Collection collection) {
        return collectionRepository.save(collection);
    }

    @Override
    public void deleteCollection(Long id) {
        try {
            collectionRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            log.debug("no collection with this id" + id + " can be found.", ex.getMessage());
        }
    }

    @Override
    public boolean isCollectionExist(Collection collection) {
        return false;
    }

    @Override
    public void addBookToCollection(Long bookId, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        collection.addBook(book);

        collectionRepository.save(collection);
    }

    @Override
    public void removeBookFromCollection(Long bookId, Long collectionId) {
        Collection collection = collectionRepository.findById(collectionId)
                .orElseThrow(() -> new RuntimeException("Collection not found"));

        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        collection.removeBook(book);
        collectionRepository.save(collection);
    }

}
