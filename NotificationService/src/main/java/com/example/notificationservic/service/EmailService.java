package com.example.notificationservic.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.example.notificationservic.dto.EmailRequest;

@Service
public class EmailService {

    public String sendEmail(EmailRequest request) {

        String message = String.format(
                """
                Notification Details
                --------------------
                To      : %s
                Subject : %s
                Body    : %s
                Time    : %s
                """,
                request.getTo(),
                request.getSubject(),
                request.getBody(),
                LocalDateTime.now());

        System.out.println(message);

        return "Email Sent Successfully";
    }
}
