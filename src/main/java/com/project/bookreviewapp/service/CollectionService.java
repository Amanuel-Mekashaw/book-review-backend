package com.project.bookreviewapp.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.bookreviewapp.entity.Collection;

public interface CollectionService {
    Page<Collection> getAllCollection(Pageable pageable);

    Collection getCollection(Long id);

    Collection saveCollection(Collection collection);

    void deleteCollection(Long id);

    boolean isCollectionExist(Collection collection);
}
