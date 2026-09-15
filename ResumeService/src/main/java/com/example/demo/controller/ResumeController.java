package com.example.demo.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Resume;
import com.example.demo.service.ResumeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService service;

    @PostMapping("/upload")
    public String uploadResume(
            @RequestParam Long userId,
            @RequestParam MultipartFile file)
            throws IOException {

        return service.uploadResume(userId, file);
    }

    @GetMapping
    public List<Resume> getAllResumes() {

        return service.getAllResumes();
    }

    @GetMapping("/{id}")
    public Resume getResume(
            @PathVariable Long id) {

        return service.getResume(id);
    }

    @GetMapping("/user/{userId}")
    public List<Resume> getResumesByUser(
            @PathVariable Long userId) {

        return service.getResumesByUser(userId);
    }

    @GetMapping("/view/{id}")
    public ResponseEntity<Resource> viewResume(
            @PathVariable Long id)
            throws Exception {

        Resume resume = service.getResume(id);

        Path path =
                Paths.get(resume.getFilePath());

        Resource resource =
                new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                resume.getFileName() +
                                "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public String deleteResume(
            @PathVariable Long id) {

        return service.deleteResume(id);
    }
}