package com.project.bookreviewapp.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private String message;
    private int code;
    private T data;

    public ApiResponse(String message, int code) {
        this.message = message;
        this.code = code;
    }
}