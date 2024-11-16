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

    @PostMapping
    public ResponseEntity<ApiResponse<AuthorDetail>> createAuthorDetail(@RequestBody AuthorDetailDTO authorDetailDTO) {
        User user = userRepository.findById(authorDetailDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("user by this id" + authorDetailDTO.getUserId() + " not found"));

        AuthorDetail authorDetail = AuthorDetailMapper.authorDetailDtoToAuthorDetail(authorDetailDTO);

        if (user != null) {
            authorDetail = authorDetailService
                    .createAuthorDetail(AuthorDetailMapper.authorDetailDtoToAuthorDetail(authorDetailDTO));
            user.setAuthorDetails(authorDetail);
            authorDetail.setUser(user);

        }

        ApiResponse<AuthorDetail> apiResponse = new ApiResponse<>("Author detail created successfully", 201,
                authorDetail);

        return new ResponseEntity<ApiResponse<AuthorDetail>>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<AuthorDetail>> updateAuthorDetail(@RequestBody AuthorDetailDTO authorDetailDTO,
            @PathVariable Long id) {

        User user = userRepository.findById(authorDetailDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("user by this id" + authorDetailDTO.getUserId() + " not found"));

        AuthorDetail foundAuthorDetail = authorDetailService.getAuthorDetail(id)
                .orElseThrow(() -> new EntityNotFoundException("no author detail found by this id " + id));

        if (user != null) {
            foundAuthorDetail = AuthorDetailMapper.authorDetailDtoToAuthorDetail(authorDetailDTO);
            foundAuthorDetail.setUser(user);
        }

        if (foundAuthorDetail.getId() == id) {
            foundAuthorDetail = authorDetailService.createAuthorDetail(foundAuthorDetail);
            ApiResponse<AuthorDetail> apiResponse = new ApiResponse<>("Author detail created successfully", 201,
                    foundAuthorDetail);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);

        } else {
            ApiResponse<AuthorDetail> apiResponseError = new ApiResponse<AuthorDetail>("No author detail to be found",
                    404, null);
            return new ResponseEntity<>(apiResponseError, HttpStatus.OK);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteAuthorDetail(@PathVariable Long id) {
        authorDetailService.deleteAuthorDetail(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("Deleted author detail", 404);
        return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.NOT_FOUND);
    }

}
