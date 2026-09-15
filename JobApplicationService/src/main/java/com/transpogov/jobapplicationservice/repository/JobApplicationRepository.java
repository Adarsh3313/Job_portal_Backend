package com.transpogov.jobapplicationservice.repository;

import com.transpogov.jobapplicationservice.entity.ApplicationStatus;
import com.transpogov.jobapplicationservice.entity.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findByCandidateId(Long candidateId);

    List<JobApplication> findByJobId(Long jobId);

    List<JobApplication> findByStatus(ApplicationStatus status);

    Optional<JobApplication> findByJobIdAndCandidateId(Long jobId, Long candidateId);

    boolean existsByJobIdAndCandidateId(Long jobId, Long candidateId);
}
