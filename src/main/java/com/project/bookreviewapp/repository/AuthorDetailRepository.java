package com.project.bookreviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.AuthorDetail;

@Repository
public interface AuthorDetailRepository extends JpaRepository<AuthorDetail, Long> {

}
