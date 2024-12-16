package com.project.bookreviewapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.genres WHERE b.id = :id")
    Optional<Book> findByIdWithGenres(@Param("id") Long id);

    // @Query("SELECT b FROM Book b JOIN FETCH b.genres")

    // @Query("SELECT new com.project.bookreviewapp.dto.BookDTO(b.id, b.title,
    // b.isbn, b.description, b.publishedYear, b.publisher, b.pages, b.language,
    // b.genres, b.coverImage) "
    @Query("SELECT b FROM Book b LEFT JOIN FETCH b.genres WHERE b.author IS NULL OR b.author = b.author")
    List<Book> findAllWithGenres();

    @Query("SELECT b FROM Book b JOIN b.genres g WHERE g.id=:genreId")
    List<Book> findBooksByGenreId(@Param("genreId") Long genreId);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    @Query("SELECT b FROM Book b " + "JOIN b.author u " + "WHERE u.id = :userId")
    List<Book> findBooksByUserId(@Param("userId") Long userId);

    // @Query("SELECT b FROM Book b " + "JOIN b.user u " + "JOIN u.authorDetail ad "
    // + "WHERE ad.firstName = :firstName "
    // + "OR ad.lastName = :lastName")

    @Query("SELECT b FROM Book b JOIN b.author u JOIN u.authorDetails ad "
            + "WHERE ad.firstName = :firstName OR ad.lastName = :lastName")
    List<Book> findBooksByAuthorFirstNameOrLastName(@Param("firstName") String firstName,
            @Param("lastName") String lastName);

}
