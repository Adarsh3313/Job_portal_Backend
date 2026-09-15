package com.transpogov.jobapplicationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobApplicationRequestDTO {

    @NotNull(message = "jobId is required")
    private Long jobId;

    @NotNull(message = "candidateId is required")
    private Long candidateId;

    @NotBlank(message = "candidateName is required")
    private String candidateName;

    @NotBlank(message = "candidateEmail is required")
    @Email(message = "candidateEmail must be a valid email address")
    private String candidateEmail;

    private Long resumeId;

    private String coverLetter;
}
