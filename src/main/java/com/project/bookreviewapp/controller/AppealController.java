package com.project.bookreviewapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.bookreviewapp.dto.AppealDTO;
import com.project.bookreviewapp.entity.Appeal;
import com.project.bookreviewapp.service.AppealService;
import com.project.bookreviewapp.utils.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/appeal")
@Slf4j
public class AppealController {

    @Autowired
    private AppealService appealService;

    // Endpoint for admin to get appeals by state
    @GetMapping("/getappealbystate")
    public ResponseEntity<ApiResponse<List<Appeal>>> getAppealsByState(@RequestParam Appeal.AppealState state) {
        ApiResponse<List<Appeal>> apiResponse;

        List<Appeal> appeals = appealService.getAppealsByState(state);

        if (appeals != null) {
            apiResponse = new ApiResponse<>("Appeals have been found", 200, appeals);
            return ResponseEntity.ok(apiResponse);

        } else {
            apiResponse = new ApiResponse<>("no appeals found", 404, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint for users to check the status of their appeal by email and state
    @GetMapping("/checkstatus")
    public ResponseEntity<ApiResponse<Appeal>> checkAppealStatus(@RequestParam String email) {

        ApiResponse<Appeal> apiResponse;

        Appeal appeal = appealService.getAppealByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("appeal with this email not found"));

        if (appeal != null) {
            apiResponse = new ApiResponse<>("Appeals have been found", 404, appeal);
            return ResponseEntity.ok(apiResponse);

        } else {
            apiResponse = new ApiResponse<>("no appeals found", 404, null);
            return ResponseEntity.ok(apiResponse);
        }

    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<String>> createAppeal(@RequestBody @Valid AppealDTO appealDTO) {
        ApiResponse<String> apiResponse;

        if (appealDTO.getEmail() != null && appealDTO.getMessage() != null) {

            appealService.createAppeal(appealDTO.getEmail(), appealDTO.getMessage());
            apiResponse = new ApiResponse<>("Appeal sent successfully", 201, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
        } else {
            apiResponse = new ApiResponse<>("Appeal didn't sent", 400, null);
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{appealId}/process")
    public ResponseEntity<ApiResponse<String>> processAppeal(
            @PathVariable Long appealId,
            @RequestParam Appeal.AppealState state,
            @RequestParam Long reviewerId,
            @RequestParam String reviewerMessage) {

        try {
            // Validate inputs
            if (reviewerId == null || state == null || reviewerMessage == null || reviewerMessage.trim().isEmpty()) {
                throw new IllegalArgumentException("Invalid input: All parameters are required.");
            }

            // Process the appeal
            Appeal appeal = appealService.processAppeal(appealId, state, reviewerId, reviewerMessage);

            // Determine the response message based on appeal state
            String message = switch (appeal.getAppealState()) {
                case APPROVED -> "Appeal has been approved. Welcome!";
                case REJECTED -> "Appeal has been rejected.";
                default -> "Appeal is still pending.";
            };

            // Return success response
            return ResponseEntity.ok(new ApiResponse<>(message, 200, null));

        } catch (IllegalArgumentException ex) {
            // Handle validation errors
            log.error("Validation error: {}", ex.getMessage());
            return ResponseEntity.badRequest()
                    .body(new ApiResponse<>(ex.getMessage(), 400, null));

        } catch (UsernameNotFoundException ex) {
            // Handle specific exceptions
            log.error("Processing error: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(new ApiResponse<>(ex.getMessage(), 403, null));

        } catch (Exception ex) {
            // Log the error and return a generic error response
            log.error("Error processing appeal: {}", ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>("An unexpected error occurred.", 500, null));
        }
    }

}
