package com.project.bookreviewapp.mapper;

import org.springframework.beans.BeanUtils;

import com.project.bookreviewapp.dto.GenreDTO;
import com.project.bookreviewapp.entity.Genre;

public class GenreMapper {

    public static GenreDTO genreToGenreDTO(Genre genre) {
        GenreDTO genreDTO = new GenreDTO();
        BeanUtils.copyProperties(genre, genreDTO);

        return genreDTO;
    }

    public static Genre genreDTOToGenre(GenreDTO genreDTO) {
        Genre genre = new Genre();
        BeanUtils.copyProperties(genreDTO, genre);

        return genre;
    }
}
