package com.transpogov.jobapplicationservice.service;

import com.transpogov.jobapplicationservice.dto.JobApplicationRequestDTO;
import com.transpogov.jobapplicationservice.dto.JobApplicationResponseDTO;
import com.transpogov.jobapplicationservice.dto.StatusUpdateDTO;

import java.util.List;

public interface JobApplicationService {

    JobApplicationResponseDTO apply(JobApplicationRequestDTO request);

    JobApplicationResponseDTO getById(Long id);

    List<JobApplicationResponseDTO> getAll();

    List<JobApplicationResponseDTO> getByCandidateId(Long candidateId);

    List<JobApplicationResponseDTO> getByJobId(Long jobId);

    JobApplicationResponseDTO updateStatus(Long id, StatusUpdateDTO statusUpdateDTO);

    void withdraw(Long id);

    void delete(Long id);
}
