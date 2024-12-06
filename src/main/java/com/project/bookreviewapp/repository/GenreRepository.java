package com.project.bookreviewapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.Genre;

@Repository

public interface GenreRepository extends JpaRepository<Genre, Long> {

    List<Genre> findAllById(Iterable<Long> ids);
}
