package com.transpogov.jobapplicationservice.dto;

import com.transpogov.jobapplicationservice.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusUpdateDTO {

    @NotNull(message = "status is required")
    private ApplicationStatus status;

    // Optional note, e.g. rejection reason or interview details, forwarded in the notification
    private String remarks;
}
