package com.project.bookreviewapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b JOIN FETCH b.genres WHERE b.id = :id")
    Optional<Book> findByIdWithGenres(@Param("id") Long id);

    // @Query("SELECT b FROM Book b JOIN FETCH b.genres")
    // @Query("SELECT b FROM Book b JOIN FETCH b.genres WHERE b.author IS NULL OR
    // b.author = b.author")
    // @Query("SELECT new com.project.bookreviewapp.dto.BookDTO(b.id, b.title,
    // b.isbn, b.description, b.publishedYear, b.publisher, b.pages, b.language,
    // b.genres, b.coverImage) "

    @Query("""
                SELECT new com.example.dto.BookDTO(
                    b.id,
                    b.title,
                    b.isbn,
                    b.description,
                    b.author.id,
                    b.publishedYear,
                    b.publisher,
                    b.pages,
                    b.language,
                    b.coverImage,
                    b.createdAt,
                    b.updatedAt,
                    (SELECT GROUP_CONCAT(g.id) FROM b.genres g)
                )
                FROM Book b
            """)
    List<Book> findAllWithGenres();

    @Query("SELECT DISTINCT b FROM Book b JOIN b.genres g WHERE g.id=:genreId")
    List<Book> findBooksByGenreId(@Param("genreId") Long genreId);
}
