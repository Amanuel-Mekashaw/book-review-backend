package com.project.bookreviewapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.dto.CollectionDTO;
import com.project.bookreviewapp.entity.Collection;
import com.project.bookreviewapp.entity.User;
import com.project.bookreviewapp.mapper.CollectionMapper;
import com.project.bookreviewapp.repository.UserRepository;
import com.project.bookreviewapp.service.CollectionService;
import com.project.bookreviewapp.utils.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/collections")
public class CollectionController {

    private final CollectionService collectionService;
    private final UserRepository userRepository;

    public CollectionController(CollectionService collectionService, UserRepository userRepository) {
        this.collectionService = collectionService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Page<Collection>> getAllCollection(@PageableDefault(size = 10) Pageable pageable) {
        Page<Collection> collections = collectionService.getAllCollection(pageable);

        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collection> getCollection(@PathVariable Long id) {
        return new ResponseEntity<>(collectionService.getCollection(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Collection>> saveCollection(@RequestBody @Valid CollectionDTO collectionDto) {
        Collection collection = collectionService
                .saveCollection(CollectionMapper.collectionDTOToCollection(collectionDto));

        ApiResponse<Collection> apiResponse = new ApiResponse<Collection>("Collection saved", 201, collection);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Collection>> updateCollection(@RequestBody @Valid CollectionDTO collectionDTO,
            @PathVariable Long id) {

        Collection foundCollection = collectionService.getCollection(collectionDTO.getId());
        User foundUser = userRepository.findById(collectionDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("user by this id " + collectionDTO.getUserId() + " isn't found"));

        if (foundCollection != null && foundUser != null) {
            foundCollection = CollectionMapper.collectionDTOToCollection(collectionDTO);
            foundCollection.setUser(foundUser);
            foundCollection.setCreatedAt(LocalDateTime.now());
            foundCollection.setUpdatedAt(LocalDateTime.now());
            foundCollection = collectionService.saveCollection(foundCollection);

            ApiResponse<Collection> apiResponse = new ApiResponse<>("collection updated successfully", 200,
                    foundCollection);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            ApiResponse<Collection> apiResponse = new ApiResponse<>("collection disn't update", 200, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

    }

    @DeleteMapping("/id")
    public ResponseEntity<ApiResponse<String>> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("collection deleted successfully", 404);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

}
