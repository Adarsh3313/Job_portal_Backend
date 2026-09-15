package com.example.hiringservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.hiringservice.dto.JobRequest;
import com.example.hiringservice.entity.Job;
import com.example.hiringservice.service.JobService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5174")
public class JobController {

    private final JobService jobService;

    @PostMapping
    public String createJob(
            @RequestBody JobRequest request) {

        return jobService.createJob(request);
    }

    @GetMapping
    public List<Job> getAllJobs() {

        return jobService.getAllJobs();
    }

    @GetMapping("/{id}")
    public Job getJobById(
            @PathVariable Long id) {

        return jobService.getJobById(id);
    }

    @PutMapping("/{id}")
    public String updateJob(
            @PathVariable Long id,
            @RequestBody JobRequest request) {

        return jobService.updateJob(id, request);
    }

    @DeleteMapping("/{id}")
    public String deleteJob(
            @PathVariable Long id) {

        return jobService.deleteJob(id);
    }

    @GetMapping("/location/{location}")
    public List<Job> getJobsByLocation(
            @PathVariable String location) {

        return jobService.getJobsByLocation(location);
    }

    @GetMapping("/search/{title}")
    public List<Job> searchJobsByTitle(
            @PathVariable String title) {

        return jobService.searchJobsByTitle(title);
    }
}