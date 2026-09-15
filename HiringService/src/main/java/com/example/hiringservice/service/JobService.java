package com.example.hiringservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.hiringservice.dto.JobRequest;
import com.example.hiringservice.entity.Job;
import com.example.hiringservice.repository.JobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class JobService {

    private final JobRepository jobRepository;

    public String createJob(JobRequest request) {

        Job job = Job.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .location(request.getLocation())
                .salary(request.getSalary())
                .status("OPEN")
                .build();

        jobRepository.save(job);

        return "Job Created Successfully";
    }

    public List<Job> getAllJobs() {

        return jobRepository.findAll();
    }

    public Job getJobById(Long id) {

        return jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job Not Found"));
    }

    public String updateJob(Long id, JobRequest request) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job Not Found"));

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());

        jobRepository.save(job);

        return "Job Updated Successfully";
    }

    public String deleteJob(Long id) {

        Job job = jobRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Job Not Found"));

        jobRepository.delete(job);

        return "Job Deleted Successfully";
    }

    public List<Job> getJobsByLocation(String location) {

        return jobRepository.findByLocation(location);
    }

    public List<Job> searchJobsByTitle(String title) {

        return jobRepository.findByTitleContainingIgnoreCase(title);
    }
}