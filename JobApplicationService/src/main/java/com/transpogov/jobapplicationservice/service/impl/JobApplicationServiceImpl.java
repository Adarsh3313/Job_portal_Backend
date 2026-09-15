package com.transpogov.jobapplicationservice.service.impl;

import com.transpogov.jobapplicationservice.client.HiringServiceClient;
import com.transpogov.jobapplicationservice.client.NotificationServiceClient;
import com.transpogov.jobapplicationservice.dto.*;
import com.transpogov.jobapplicationservice.entity.ApplicationStatus;
import com.transpogov.jobapplicationservice.entity.JobApplication;
import com.transpogov.jobapplicationservice.exception.ResourceNotFoundException;
import com.transpogov.jobapplicationservice.repository.JobApplicationRepository;
import com.transpogov.jobapplicationservice.service.JobApplicationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobApplicationServiceImpl implements JobApplicationService {

    private final JobApplicationRepository repository;
    private final HiringServiceClient hiringServiceClient;
    private final NotificationServiceClient notificationServiceClient;

    @Override
    @Transactional
    public JobApplicationResponseDTO apply(JobApplicationRequestDTO request) {

        // 1. Validate the job exists via HiringService (Feign, load-balanced through Eureka)
        JobDTO job = fetchJob(request.getJobId());

        // 2. Prevent duplicate applications by the same candidate to the same job
        if (repository.existsByJobIdAndCandidateId(request.getJobId(), request.getCandidateId())) {
            throw new IllegalStateException(
                    "Candidate " + request.getCandidateId() + " has already applied to job " + request.getJobId());
        }

        // 3. Persist the application
        JobApplication application = JobApplication.builder()
                .jobId(request.getJobId())
                .candidateId(request.getCandidateId())
                .candidateName(request.getCandidateName())
                .candidateEmail(request.getCandidateEmail())
                .resumeId(request.getResumeId())
                .coverLetter(request.getCoverLetter())
                .status(ApplicationStatus.APPLIED)
                .build();

        JobApplication saved = repository.save(application);

        // 4. Notify the candidate (best-effort; do not fail the application if notification fails)
        String jobTitle = job != null ? job.getTitle() : ("job #" + request.getJobId());
        notifySafely(saved.getCandidateEmail(), saved.getCandidateName(),
                "Application received",
                "Hi " + saved.getCandidateName() + ", your application for '" + jobTitle
                        + "' has been received and is now " + saved.getStatus() + ".");

        return toResponseDTO(saved);
    }

    @Override
    public JobApplicationResponseDTO getById(Long id) {
        return toResponseDTO(findByIdOrThrow(id));
    }

    @Override
    public List<JobApplicationResponseDTO> getAll() {
        return repository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplicationResponseDTO> getByCandidateId(Long candidateId) {
        return repository.findByCandidateId(candidateId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<JobApplicationResponseDTO> getByJobId(Long jobId) {
        return repository.findByJobId(jobId).stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public JobApplicationResponseDTO updateStatus(Long id, StatusUpdateDTO statusUpdateDTO) {
        JobApplication application = findByIdOrThrow(id);
        application.setStatus(statusUpdateDTO.getStatus());
        JobApplication saved = repository.save(application);

        String remarks = statusUpdateDTO.getRemarks();
        String message = "Hi " + saved.getCandidateName() + ", your application (ID: " + saved.getId()
                + ") status has been updated to " + saved.getStatus() + "."
                + (remarks != null && !remarks.isBlank() ? " Note: " + remarks : "");

        notifySafely(saved.getCandidateEmail(), saved.getCandidateName(), "Application status updated", message);

        return toResponseDTO(saved);
    }

    @Override
    @Transactional
    public void withdraw(Long id) {
        JobApplication application = findByIdOrThrow(id);
        application.setStatus(ApplicationStatus.WITHDRAWN);
        repository.save(application);
        notifySafely(application.getCandidateEmail(), application.getCandidateName(),
                "Application withdrawn",
                "Hi " + application.getCandidateName() + ", your application (ID: " + application.getId()
                        + ") has been withdrawn.");
    }

    @Override
    @Transactional
    public void delete(Long id) {
        JobApplication application = findByIdOrThrow(id);
        repository.delete(application);
    }

    // ---------- helpers ----------

    private JobApplication findByIdOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job application not found with id: " + id));
    }

    private JobDTO fetchJob(Long jobId) {
        try {
            JobDTO job = hiringServiceClient.getJobById(jobId);
            if (job == null) {
                throw new ResourceNotFoundException("Job not found with id: " + jobId);
            }
            return job;
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Job not found with id: " + jobId);
        } catch (FeignException e) {
            // HiringService is down or errored - log and continue without blocking the application
            log.warn("Could not reach HiringService to validate job {}: {}", jobId, e.getMessage());
            return null;
        }
    }

    private void notifySafely(String email, String name, String subject, String message) {
        try {
            notificationServiceClient.sendNotification(
                    NotificationRequestDTO.builder()
                            .recipientEmail(email)
                            .recipientName(name)
                            .subject(subject)
                            .message(message)
                            .build()
            );
        } catch (FeignException e) {
            // Never fail the primary workflow just because NotificationService is unavailable
            log.warn("Could not send notification to {}: {}", email, e.getMessage());
        }
    }

    private JobApplicationResponseDTO toResponseDTO(JobApplication a) {
        return JobApplicationResponseDTO.builder()
                .id(a.getId())
                .jobId(a.getJobId())
                .candidateId(a.getCandidateId())
                .candidateName(a.getCandidateName())
                .candidateEmail(a.getCandidateEmail())
                .resumeId(a.getResumeId())
                .coverLetter(a.getCoverLetter())
                .status(a.getStatus())
                .appliedDate(a.getAppliedDate())
                .lastUpdated(a.getLastUpdated())
                .build();
    }
}
