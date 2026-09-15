package com.example.jobapplicationservice.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.notificationservic.dto.EmailRequest;

@FeignClient(name = "NOTIFICATION-SERVICE")
public interface NotificationFeignClient {

    @PostMapping("/api/notifications/email")
    String sendEmail(@RequestBody EmailRequest request);
}