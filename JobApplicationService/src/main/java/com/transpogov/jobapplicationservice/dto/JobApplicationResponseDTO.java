package com.transpogov.jobapplicationservice.dto;

import com.transpogov.jobapplicationservice.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobApplicationResponseDTO {

    private Long id;
    private Long jobId;
    private Long candidateId;
    private String candidateName;
    private String candidateEmail;
    private Long resumeId;
    private String coverLetter;
    private ApplicationStatus status;
    private LocalDateTime appliedDate;
    private LocalDateTime lastUpdated;
}
