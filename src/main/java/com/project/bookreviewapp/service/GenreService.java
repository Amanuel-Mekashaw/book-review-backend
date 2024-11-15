package com.project.bookreviewapp.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.project.bookreviewapp.entity.Genre;

public interface GenreService {
    Optional<Genre> getGenre(Long id);

    Page<Genre> getAllGenre(Pageable pageable);

    Genre createGenre(Genre genre);

    void deleteGenre(Long id);

    boolean isGenreExist(Genre genre);
}
