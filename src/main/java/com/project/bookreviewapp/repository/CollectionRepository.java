package com.project.bookreviewapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.Collection;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    List<Collection> findAllById(Iterable<Long> ids);


    // Fetch private collections for a specific user
    List<Collection> findByUser_IdAndIsPrivate(Long userId, boolean isPrivate);

    // Fetch all public collections
    List<Collection> findByIsPrivate(boolean isPrivate);

}
