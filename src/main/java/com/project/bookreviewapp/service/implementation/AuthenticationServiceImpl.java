package com.project.bookreviewapp.service.implementation;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;

import javax.naming.AuthenticationException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.bookreviewapp.auth.AuthenticationRequest;
import com.project.bookreviewapp.auth.AuthenticationResponse;
import com.project.bookreviewapp.auth.RegisterRequest;
import com.project.bookreviewapp.config.JwtService;
import com.project.bookreviewapp.entity.Appeal.AppealState;
import com.project.bookreviewapp.entity.Collection;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.entity.User.Role;
import com.project.bookreviewapp.entity.User.Status;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.AuthenticationService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {

        var user = User.builder().userName(request.getUsername()).email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword())).role(Role.USER).status(Status.ACTIVE)
                .createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws Exception {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            var user = repository.findByEmail(request.getEmail()).orElseThrow(
                    () -> new AuthenticationException("user with email" + request.getEmail() + "not found"));

            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).user(user).build();
        } catch (BadCredentialsException ex) {
            log.error("Authentication failed for user: {} - Invalid credentials", request.getEmail());
            log.debug("{ email: " + request.getEmail() + ',' + "password: " + request.getPassword() + " }");
            throw new AuthenticationException("Invalid credentials" + ex);
        } catch (Exception ex) {
            log.error("Unexpected error during authentication: {}", ex.getMessage());
            throw new RuntimeException("An error occurred during authentication", ex);
        }

    }

    public void assignRole(String token, String username, String role) throws Exception {
        // Extract the role of the requester
        String requesterRole = jwtService.extractRole(token.replace("Bearer ", ""));

        if (!"ADMIN".equalsIgnoreCase(requesterRole)) {
            throw new AccessDeniedException("Only ADMIN can assign roles.");
        }

        // Validate the role being assigned
        Role newRole;

        try {
            newRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + role);
        }

        // Fetch the user and assign the role
        User user = repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        user.setRole(newRole);
        repository.save(user);

        log.info("Role {} assigned to user {}", newRole, username);
    }

    // change status
    @Override
    public void assignStatus(String token, String email, Status state) throws Exception {
        // Extract the role of the requester
        String requesterRole = jwtService.extractRole(token.replace("Bearer ", ""));

        if (!"ADMIN".equalsIgnoreCase(requesterRole)) {
            throw new AccessDeniedException("Only ADMIN can assign roles.");
        }

        Status appealState;

        try {
            appealState = Status.valueOf(state.toString());

            // Fetch the user and assign the role
            User user = repository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
            user.setStatus(appealState);
            repository.save(user);

            log.info("Status {} assigned to user {}", state, email);

        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid role: " + String.valueOf(state));
        }
    }

    @Override
    public List<User> getAllUsers(String token) throws Exception {
        List<User> users = repository.findAll();
        return users;
    }

    @Override
    public void deleteUserById(Long id, String token) throws Exception {
        try {
            String requesterRole = jwtService.extractRole(token.replace("Bearer ", ""));

            if (!"ADMIN".equalsIgnoreCase(requesterRole)) {
                throw new AccessDeniedException("Only ADMIN can delete users.");
            } else {
                User user = repository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("User by this id: " + id + " Not found"));

                if (user != null) {
                    // remove author detail info
                    user.setAuthorDetails(null);
                    // remove collection where the author is referenced
                    for (Collection collection : user.getCollections()) {
                        user.getCollections().remove(collection);
                    }
                    repository.deleteById(id);
                }

            }
        } catch (EmptyResultDataAccessException ex) {
            log.debug("user with " + id + " doesn't exist\n\n" + ex.getMessage());
        }
    }

    @Override
    public User findUserById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("user with id " + id + "not found"));
    }

}
