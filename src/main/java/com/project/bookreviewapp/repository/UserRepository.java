package com.project.bookreviewapp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.entity.User.Role;
import com.project.bookreviewapp.entity.User.Status;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);

    // User findByUsername(String userName);

    // general paginated list retrieving
    Page<User> findByStatus(Status status, Pageable pageable);

    Page<User> findByRole(Role role, Pageable pageable);

    // non paginated list retrieving

    List<User> findByStatus(Status status);

    List<User> findByRole(Role role);

    // Optional<User> findByUsername(String userName);

    // Optional<User> findById(@NonNull Long id);

}
