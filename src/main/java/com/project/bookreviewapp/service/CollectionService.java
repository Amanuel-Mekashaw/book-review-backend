package com.project.bookreviewapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.bookreviewapp.entity.Collection;

public interface CollectionService {
    Page<Collection> getAllCollection(Pageable pageable);

    Collection getCollection(Long id);

    Collection saveCollection(Collection collection);

    void deleteCollection(Long id);

    boolean isCollectionExist(Collection collection);

    public void addBookToCollection(Long bookId, Long collectionId);

    public void removeBookFromCollection(Long bookId, Long collectionId);

    public List<Collection> getPrivateCollection(Long userId);
    public List<Collection> getPublicCollection();

}
