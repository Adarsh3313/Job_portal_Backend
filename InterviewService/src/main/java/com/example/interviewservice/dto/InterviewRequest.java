package com.example.interviewservice.dto;

import lombok.Data;

@Data
public class InterviewRequest {

    private Long applicationId;

    private String candidateName;

    private String candidateEmail;

    private String interviewDate;

    private String interviewTime;

    private String mode;

    private String meetingLink;

    private String interviewer;
}