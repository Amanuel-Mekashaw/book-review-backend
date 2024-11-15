package com.project.bookreviewapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
