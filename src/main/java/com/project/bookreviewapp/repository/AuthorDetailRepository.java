package com.project.bookreviewapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.AuthorDetail;
import com.project.bookreviewapp.entity.User;

@Repository
public interface AuthorDetailRepository extends JpaRepository<AuthorDetail, Long> {

    @Query("SELECT a.user FROM AuthorDetail a WHERE a.user.id = :authorId")
    Optional<User> findUserByAuthorId(@Param("authorId") Long authorId);

    @Query("SELECT a FROM AuthorDetail a WHERE a.user.id = :authorId")
    Optional<AuthorDetail> findUserDetailByAuthorId(@Param("authorId") Long authorId);
}
