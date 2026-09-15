package com.transpogov.jobapplicationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Adjust field names here if NotificationService's actual request shape differs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequestDTO {
    private String recipientEmail;
    private String recipientName;
    private String subject;
    private String message;
}
