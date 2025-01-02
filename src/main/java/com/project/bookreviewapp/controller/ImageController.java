package com.project.bookreviewapp.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private static final String UPLOAD_DIR = "D:\\test\\";

    @GetMapping("/{type}/{filename}")
    public ResponseEntity<Resource> serveImage(@PathVariable String type, @PathVariable String filename) {

        // Validate type (allow only book_images and profile_images)
        if (!"book_images".equals(type) && !"profile_images".equals(type)) {
            return ResponseEntity.badRequest().build();
        }

        try {

            Path filePath = Paths.get(UPLOAD_DIR, type, filename);
            System.out.println("\n\n\nFile path: " + filePath.toAbsolutePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new RuntimeException("Could not read the file: " + filename);
            }

            // Determine content type
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
