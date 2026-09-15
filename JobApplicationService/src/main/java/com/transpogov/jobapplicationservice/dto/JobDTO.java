package com.transpogov.jobapplicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mirrors the fields we need from HiringService's Job resource.
 * Adjust field names here if HiringService's actual response shape differs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JobDTO {
    private Long id;
    private String title;
    private String companyName;
    private String location;
    private boolean active;
}
