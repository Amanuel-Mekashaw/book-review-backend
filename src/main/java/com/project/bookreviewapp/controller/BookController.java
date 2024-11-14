package com.project.bookreviewapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    @GetMapping
    public ResponseEntity<String> sayHello() {
        return new ResponseEntity<String>("Books", HttpStatus.OK);
    }

}
