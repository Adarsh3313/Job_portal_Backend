package com.example.demo.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HiringDepartmentController {

    @PreAuthorize("hasRole('HIRING_DEPARTMENT')")
    @GetMapping("/hiring")
    public String hiring() {
        return "Welcome Hiring Department";
    }
}