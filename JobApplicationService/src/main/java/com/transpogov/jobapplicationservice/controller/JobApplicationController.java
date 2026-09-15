package com.transpogov.jobapplicationservice.controller;

import com.transpogov.jobapplicationservice.dto.JobApplicationRequestDTO;
import com.transpogov.jobapplicationservice.dto.JobApplicationResponseDTO;
import com.transpogov.jobapplicationservice.dto.StatusUpdateDTO;
import com.transpogov.jobapplicationservice.service.JobApplicationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class JobApplicationController {

    private final JobApplicationService jobApplicationService;

    // Candidate applies to a job
    @PostMapping
    public ResponseEntity<JobApplicationResponseDTO> apply(@Valid @RequestBody JobApplicationRequestDTO request) {
        return new ResponseEntity<>(jobApplicationService.apply(request), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<JobApplicationResponseDTO>> getAll() {
        return ResponseEntity.ok(jobApplicationService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplicationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(jobApplicationService.getById(id));
    }

    @GetMapping("/candidate/{candidateId}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getByCandidateId(@PathVariable Long candidateId) {
        return ResponseEntity.ok(jobApplicationService.getByCandidateId(candidateId));
    }

    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<JobApplicationResponseDTO>> getByJobId(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobApplicationService.getByJobId(jobId));
    }

    // Used by HiringService/InterviewService staff to move an application through its lifecycle
    @PatchMapping("/{id}/status")
    public ResponseEntity<JobApplicationResponseDTO> updateStatus(@PathVariable Long id,
                                                                    @Valid @RequestBody StatusUpdateDTO statusUpdateDTO) {
        return ResponseEntity.ok(jobApplicationService.updateStatus(id, statusUpdateDTO));
    }

    // Candidate withdraws their own application
    @PatchMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdraw(@PathVariable Long id) {
        jobApplicationService.withdraw(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        jobApplicationService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
