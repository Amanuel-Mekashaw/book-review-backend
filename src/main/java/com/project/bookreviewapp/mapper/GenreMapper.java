package com.project.bookreviewapp.mapper;

import com.project.bookreviewapp.dto.GenreDTO;
import com.project.bookreviewapp.entity.Genre;

public class GenreMapper {

    public static GenreDTO genreDTOToGenre(Genre genre) {
        return GenreDTO.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }

    public static Genre genreToGenreDTO(GenreDTO genre) {
        return Genre.builder()
                .id(genre.getId())
                .name(genre.getName())
                .build();
    }
}
