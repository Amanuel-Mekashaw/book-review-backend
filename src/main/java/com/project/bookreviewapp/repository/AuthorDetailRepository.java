package com.project.bookreviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.bookreviewapp.entity.AuthorDetail;

public interface AuthorDetailRepository extends JpaRepository<AuthorDetail, Long> {

}
