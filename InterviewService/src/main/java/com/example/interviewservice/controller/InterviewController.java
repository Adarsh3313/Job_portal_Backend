package com.example.interviewservice.controller;

import java.util.List;

import org.springframework.web.bind.annotation.*;

import com.example.interviewservice.dto.InterviewRequest;
import com.example.interviewservice.entity.Interview;
import com.example.interviewservice.service.InterviewService;

import lombok.RequiredArgsConstructor;

@RestController
@CrossOrigin(origins = "http://localhost:5174")
@RequestMapping("/api/interviews")
@RequiredArgsConstructor
public class InterviewController {

    private final InterviewService service;

    @PostMapping
    public Interview scheduleInterview(
            @RequestBody InterviewRequest request) {

        return service.scheduleInterview(request);
    }

    @GetMapping
    public List<Interview> getAllInterviews() {

        return service.getAllInterviews();
    }

    @GetMapping("/candidate/{email}")
    public List<Interview> getCandidateInterviews(
            @PathVariable String email) {

        return service.getCandidateInterviews(email);
    }
}