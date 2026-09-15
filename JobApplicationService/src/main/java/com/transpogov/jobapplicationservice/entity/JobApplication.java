package com.transpogov.jobapplicationservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "job_applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foreign reference to HiringService's job posting
    @Column(name = "job_id", nullable = false)
    private Long jobId;

    // Foreign reference to AuthenticationService / candidate user id
    @Column(name = "candidate_id", nullable = false)
    private Long candidateId;

    @Column(name = "candidate_name")
    private String candidateName;

    @Column(name = "candidate_email", nullable = false)
    private String candidateEmail;

    // Foreign reference to ResumeService
    @Column(name = "resume_id")
    private Long resumeId;

    @Column(name = "cover_letter", length = 2000)
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ApplicationStatus status;

    @Column(name = "applied_date", nullable = false)
    private LocalDateTime appliedDate;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    @PrePersist
    protected void onCreate() {
        this.appliedDate = LocalDateTime.now();
        this.lastUpdated = LocalDateTime.now();
        if (this.status == null) {
            this.status = ApplicationStatus.APPLIED;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.lastUpdated = LocalDateTime.now();
    }
}
