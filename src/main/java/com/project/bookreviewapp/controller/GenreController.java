package com.project.bookreviewapp.controller;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.dto.GenreDTO;
import com.project.bookreviewapp.entity.Genre;
import com.project.bookreviewapp.mapper.GenreMapper;
import com.project.bookreviewapp.service.GenreService;
import com.project.bookreviewapp.utils.ApiResponse;

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
        Genre genre = genreService.getGenre(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id " + id + " not found"));

        return new ResponseEntity<Genre>(genre, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Genre>> createGenre(@RequestBody GenreDTO genreDTO) {
        Genre genre = genreService.createGenre(GenreMapper.genreDTOToGenre(genreDTO));

        ApiResponse<Genre> apiResponse = new ApiResponse<Genre>("Genre created successfully", 201, genre);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Genre>> updateGenre(@RequestBody GenreDTO genreDTO, @PathVariable Long id) {
        Genre foundGenre = genreService.getGenre(id)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id " + id + "not found"));

        if (foundGenre != null) {
            foundGenre.setCreatedAt(foundGenre.getCreatedAt());
            foundGenre.setUpdatedAt(LocalDateTime.now());
            foundGenre = genreService.createGenre(GenreMapper.genreDTOToGenre(genreDTO));

            ApiResponse<Genre> apiResponse = new ApiResponse<Genre>("Genre created successfully", 200, foundGenre);

            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            ApiResponse<Genre> apiResponse = new ApiResponse<Genre>("Genre with id " + id + "not found", 404);
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

    }

    @DeleteMapping("/id")
    public ResponseEntity<ApiResponse<String>> deleteGenre(@PathVariable Long id) {
        genreService.deleteGenre(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("genre deleted successfully", 404);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

}
