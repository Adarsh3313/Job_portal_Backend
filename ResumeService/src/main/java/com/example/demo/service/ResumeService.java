package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entity.Resume;
import com.example.demo.repository.ResumeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository repository;

    private static final String UPLOAD_DIR = "uploads";

    public String uploadResume(Long userId,
                               MultipartFile file) throws IOException {

        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName =
                System.currentTimeMillis()
                        + "_"
                        + file.getOriginalFilename();

        Path filePath = uploadPath.resolve(fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING);

        Resume resume = Resume.builder()
                .userId(userId)
                .fileName(file.getOriginalFilename())
                .fileType(file.getContentType())
                .filePath(filePath.toString())
                .uploadedAt(LocalDateTime.now())
                .build();

        repository.save(resume);

        return "Resume Uploaded Successfully";
    }

    public List<Resume> getAllResumes() {
        return repository.findAll();
    }

    public Resume getResume(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resume Not Found"));
    }

    public List<Resume> getResumesByUser(Long userId) {

        return repository.findByUserId(userId);
    }

    public String deleteResume(Long id) {

        Resume resume = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Resume Not Found"));

        try {

            Path filePath =
                    Paths.get(resume.getFilePath());

            Files.deleteIfExists(filePath);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Unable to delete file");
        }

        repository.delete(resume);

        return "Resume Deleted Successfully";
    }
}