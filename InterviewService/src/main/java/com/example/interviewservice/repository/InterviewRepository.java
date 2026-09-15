package com.example.interviewservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.interviewservice.entity.Interview;

public interface InterviewRepository
        extends JpaRepository<Interview, Long> {

    List<Interview> findByCandidateEmail(
            String candidateEmail);

    List<Interview> findByApplicationId(
            Long applicationId);
}