package com.parent.tenant.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/files")
public class FileController {

    @GetMapping("/profile-photos/{filename:.+}")
    public ResponseEntity<Resource> serve(@PathVariable String filename) throws Exception {
        Path uploadDir = Paths.get("uploads/profile-photos");
        Path file = uploadDir.resolve(filename);
        Resource resource = new UrlResource(file.toUri());
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG) // generic; you can detect content type
                .body(resource);
    }
}
