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

    @PostMapping("/{collectionId}/books/{bookId}")
    public ResponseEntity<ApiResponse<String>> addBookToCollection(@PathVariable @Valid Long collectionId,
            @PathVariable @Valid Long bookId) {

        ApiResponse<String> apiResponse;

        try {
            collectionService.addBookToCollection(bookId, collectionId);
            apiResponse = new ApiResponse<>("Book successfully added to the collection", 200, null);
            return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            apiResponse = new ApiResponse<>(e.getMessage(), 404, null);
            return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Collection>> saveCollection(@RequestBody @Valid CollectionDTO collectionDto) {

        User foundAuthor = userRepository.findById(collectionDto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("user by " + collectionDto.getUserId() + " not found"));

        Collection collection = CollectionMapper.collectionDTOToCollection(collectionDto);
        collection.setUser(foundAuthor);
        collection = collectionService.saveCollection(collection);

        ApiResponse<Collection> apiResponse = new ApiResponse<Collection>("Collection saved", 201, collection);

        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Collection>> updateCollection(@RequestBody @Valid CollectionDTO collectionDTO,
            @PathVariable Long id) {

        Collection foundCollection = collectionService.getCollection(id);
        User foundUser = userRepository.findById(collectionDTO.getUserId()).orElseThrow(
                () -> new EntityNotFoundException("user by this id " + collectionDTO.getUserId() + " isn't found"));

        ApiResponse<Collection> apiResponse;
        if (foundCollection != null && foundUser != null) {
            foundCollection = CollectionMapper.collectionDTOToCollection(collectionDTO);
            foundCollection.setId(id);
            foundCollection = collectionService.saveCollection(foundCollection);

            apiResponse = new ApiResponse<>("collection updated successfully", 200, foundCollection);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        } else {
            apiResponse = new ApiResponse<>("collection didn't update", 401, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.OK);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCollection(@PathVariable Long id) {
        collectionService.deleteCollection(id);
        ApiResponse<String> apiResponse = new ApiResponse<>("collection deleted successfully", 404);
        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/delete/{collectionId}/books/{bookId}")
    public ResponseEntity<ApiResponse<String>> removeBookFromCollection(@PathVariable @Valid Long collectionId,
            @PathVariable @Valid Long bookId) {

        ApiResponse<String> apiResponse;

        try {
            collectionService.removeBookFromCollection(bookId, collectionId);
            apiResponse = new ApiResponse<>("Book successfully removed from the collection", 200, null);
            return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.OK);
        } catch (RuntimeException e) {
            apiResponse = new ApiResponse<>(e.getMessage(), 404, null);
            return new ResponseEntity<ApiResponse<String>>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

}
