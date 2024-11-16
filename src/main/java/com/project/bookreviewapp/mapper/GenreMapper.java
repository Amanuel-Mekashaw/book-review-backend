package com.project.bookreviewapp.mapper;

import com.project.bookreviewapp.dto.GenreDTO;
import com.project.bookreviewapp.entity.Genre;

public class GenreMapper {

    public static GenreDTO genreToGenreDTO(Genre genre) {
        return GenreDTO.builder().id(genre.getId()).name(genre.getName()).build();
    }

    public static Genre genreDTOToGenre(GenreDTO genreDTO) {
        return Genre.builder().id(genreDTO.getId()).name(genreDTO.getName()).build();
    }
}
