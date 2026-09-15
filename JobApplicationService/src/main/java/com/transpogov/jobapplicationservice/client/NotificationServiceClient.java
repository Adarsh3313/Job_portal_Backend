package com.transpogov.jobapplicationservice.client;

import com.transpogov.jobapplicationservice.dto.NotificationRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Talks to NotificationService via Eureka's service registry (name-based, load-balanced).
 * "notification-service" must match spring.application.name in NotificationService's own application.yml.
 * Adjust the path below to match NotificationService's actual exposed endpoint.
 */
@FeignClient(name = "notification-service")
public interface NotificationServiceClient {

    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationRequestDTO request);
}
