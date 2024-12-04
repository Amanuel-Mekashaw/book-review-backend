package com.project.bookreviewapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.dto.AuthorDetailDTO;
import com.project.bookreviewapp.entity.AuthorDetail;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.mapper.AuthorDetailMapper;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.AuthorDetailService;
import com.project.bookreviewapp.utils.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/userdetail")
public class AuthorDetailController {

    private final AuthorDetailService authorDetailService;
    private final UserRepository userRepository;

    public AuthorDetailController(AuthorDetailService authorDetailService, UserRepository userRepository) {
        this.authorDetailService = authorDetailService;
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDetail> getUserDetail(@PathVariable Long id) {
        AuthorDetail authorDetail = authorDetailService.getAuthorDetail(id)
                .orElseThrow(() -> new EntityNotFoundException("user detail not found"));
        return new ResponseEntity<AuthorDetail>(authorDetail, HttpStatus.OK);
    }

    // @GetMapping("/by-author/{authorId}")
    // public ResponseEntity<ApiResponse<User>> getUserByAuthorId(@PathVariable Long
    // authorId) {
    // User user = authorDetailService.getUserByAuthorId(authorId)
    // .orElseThrow(() -> new EntityNotFoundException("\n\n\nUser with id" +
    // authorId + "not found\n\n\n"));
    // ApiResponse<User> apiResponse;
    // if (user == null) {
    // apiResponse = new ApiResponse<User>("user found", 200, user);
    // return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    // } else {
    // apiResponse = new ApiResponse<>("user not found", 404, null);
    // return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    // }

    // }

    @GetMapping("/by-author/{authorId}")
    public ResponseEntity<ApiResponse<User>> getUserByAuthorId(@PathVariable Long authorId) {
        // Get the user or throw EntityNotFoundException if not found
        User user = authorDetailService.getUserByAuthorId(authorId)
                .orElseThrow(() -> new EntityNotFoundException("User with id " + authorId + " not found"));

        // Build API response if user is found
        ApiResponse<User> apiResponse = new ApiResponse<>("User found", 200, user);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @GetMapping("/by-authordetail/{authorId}")
    public ResponseEntity<ApiResponse<AuthorDetail>> getUserDetailByAuthorId(@PathVariable Long authorId) {
        // Get the user or throw EntityNotFoundException if not found
        AuthorDetail user = authorDetailService.getUserDetailByAuthorId(authorId)
                .orElseThrow(() -> new EntityNotFoundException("author detail with id " + authorId + " not found"));

        // Build API response if Author Detail is found
        ApiResponse<AuthorDetail> apiResponse = new ApiResponse<>("User found", 200, user);
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<AuthorDetail>> createAuthorDetail(
            @RequestBody @Valid AuthorDetailDTO authorDetailDTO) {
        User user = userRepository.findById(authorDetailDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("user by this id" + authorDetailDTO.getUserId() + " not found"));

        AuthorDetail authorDetail = AuthorDetailMapper.authorDetailDtoToAuthorDetail(authorDetailDTO);

        ApiResponse<AuthorDetail> apiResponse;
        if (user != null) {
            // user.setAuthorDetails(authorDetail);
            authorDetail = AuthorDetailMapper.authorDetailDtoToAuthorDetail(authorDetailDTO);
            authorDetail.setUser(user);
            authorDetail = authorDetailService.createAuthorDetail(authorDetail);

            apiResponse = new ApiResponse<>("Author detail created successfully", 201, authorDetail);

            return new ResponseEntity<ApiResponse<AuthorDetail>>(apiResponse, HttpStatus.CREATED);
        } else {
            apiResponse = new ApiResponse<>("Couldn't create Author detail ", 403, authorDetail);

            return new ResponseEntity<ApiResponse<AuthorDetail>>(apiResponse, HttpStatus.FORBIDDEN);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorDetail>> updateAuthorDetail(
            @RequestBody @Valid AuthorDetailDTO authorDetailDTO, @PathVariable Long id) {

        AuthorDetail foundAuthorDetail = authorDetailService.getAuthorDetail(id)
                .orElseThrow(() -> new EntityNotFoundException("no author detail found by this id " + id));

        User user = userRepository.findById(authorDetailDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("user by this id" + authorDetailDTO.getUserId() + " not found"));

        ApiResponse<AuthorDetail> apiResponse;

        if (user != null && foundAuthorDetail != null) {
            foundAuthorDetail = AuthorDetailMapper.authorDetailDtoToAuthorDetail(authorDetailDTO);
            foundAuthorDetail.setId(id);
            // foundAuthorDetail.setUser(user);
            foundAuthorDetail = authorDetailService.createAuthorDetail(foundAuthorDetail);

            apiResponse = new ApiResponse<>("Author detail updated successfully", 201, foundAuthorDetail);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<AuthorDetail>("can't update user detail", 404, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAuthorDetail(@PathVariable Long id) {
        authorDetailService.deleteAuthorDetail(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Deleted author detail", 404);
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.NOT_FOUND);
    }

}
