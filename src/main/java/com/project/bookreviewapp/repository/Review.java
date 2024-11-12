package com.project.bookreviewapp.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Review extends JpaRepository<Review, UUID> {

}
