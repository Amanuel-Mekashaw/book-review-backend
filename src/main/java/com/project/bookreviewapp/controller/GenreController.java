package com.project.bookreviewapp.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.entity.Genre;
import com.project.bookreviewapp.service.GenreService;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/genre")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public ResponseEntity<Page<Genre>> listAllGenre(@PageableDefault(size = 10) Pageable pageable) {

        Page<Genre> genres = genreService.getAllGenre(pageable);
        return new ResponseEntity<Page<Genre>>(genres, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Genre> getGenre(@PathVariable Long id) {
        Genre genre = genreService.getGenre(id).orElseThrow(
                () -> new EntityNotFoundException("Genre with id " + id + " not found"));

        return new ResponseEntity<Genre>(genre, HttpStatus.OK);
    }

}
