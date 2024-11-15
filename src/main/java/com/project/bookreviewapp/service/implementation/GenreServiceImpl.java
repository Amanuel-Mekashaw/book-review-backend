package com.project.bookreviewapp.service.implementation;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.project.bookreviewapp.entity.Genre;
import com.project.bookreviewapp.repository.GenreRepository;
import com.project.bookreviewapp.service.GenreService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;

    public GenreServiceImpl(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    @Override
    public Optional<Genre> getGenre(Long id) {
        return genreRepository.findById(id);
    }

    @Override
    public Page<Genre> getAllGenre(Pageable pageable) {
        return genreRepository.findAll(pageable);
    }

    @Override
    public Genre createGenre(Genre genre) {
        return genreRepository.save(genre);
    }

    @Override
    public void deleteGenre(Long id) {
        try {
            genreRepository.deleteById(id);
        } catch (EmptyResultDataAccessException ex) {
            log.debug("genre with this id" + id + " doesn't exist" + ex.getMessage());
        }
    }

    @Override
    public boolean isGenreExist(Genre genre) {
        return genreRepository.existsById(genre.getId());
    }

}
