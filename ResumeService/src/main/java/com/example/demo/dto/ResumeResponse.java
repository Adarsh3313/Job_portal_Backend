package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeResponse {

    private Long resumeId;
    private Long userId;
    private String fileName;
    private String fileType;
}
