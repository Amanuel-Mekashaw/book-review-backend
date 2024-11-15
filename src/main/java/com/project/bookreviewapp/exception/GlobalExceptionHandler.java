package com.project.bookreviewapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.project.bookreviewapp.utils.ApiResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.validation.BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<com.project.bookreviewapp.utils.ApiResponse<Map<String, String>>> handleBindException(
            BindingResult result) {
        Map<String, String> errorMap = new HashMap<>();

        List<FieldError> fieldErrors = result.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(
                "Validation Failed", 400, errorMap);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(jakarta.validation.ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleConstraintViolation(
            jakarta.validation.ConstraintViolationException ex) {
        Map<String, String> errorMap = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            errorMap.put(violation.getPropertyPath().toString(),
                    violation.getMessage());
        });

        ApiResponse<Map<String, String>> apiResponse = new ApiResponse<>(
                "Validation Failed", 400, errorMap);

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }
}