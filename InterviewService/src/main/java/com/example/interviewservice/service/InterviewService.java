package com.example.interviewservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.interviewservice.dto.InterviewRequest;
import com.example.interviewservice.entity.Interview;
import com.example.interviewservice.repository.InterviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InterviewService {

    private final InterviewRepository repository;

    public Interview scheduleInterview(
            InterviewRequest request) {

        Interview interview =
                Interview.builder()
                        .applicationId(
                                request.getApplicationId())
                        .candidateName(
                                request.getCandidateName())
                        .candidateEmail(
                                request.getCandidateEmail())
                        .interviewDate(
                                request.getInterviewDate())
                        .interviewTime(
                                request.getInterviewTime())
                        .mode(
                                request.getMode())
                        .meetingLink(
                                request.getMeetingLink())
                        .interviewer(
                                request.getInterviewer())
                        .status("SCHEDULED")
                        .build();

        return repository.save(interview);
    }

    public List<Interview> getAllInterviews() {
        return repository.findAll();
    }

    public List<Interview> getCandidateInterviews(
            String email) {

        return repository.findByCandidateEmail(
                email);
    }
}