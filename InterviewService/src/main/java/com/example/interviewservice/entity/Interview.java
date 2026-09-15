package com.example.interviewservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "interviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Interview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewId;

    private Long applicationId;

    private String candidateName;

    private String candidateEmail;

    private String interviewDate;

    private String interviewTime;

    private String mode;

    private String meetingLink;

    private String interviewer;

    private String status;
}