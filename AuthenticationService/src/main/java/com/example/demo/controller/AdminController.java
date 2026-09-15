package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.example.demo.entity.User;
import com.example.demo.enums.Role;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final UserRepository userRepository;

    @GetMapping("/pending-recruiters")
    public List<User> getPendingRecruiters() {

        return userRepository.findByRoleAndApprovedFalse(
                Role.HIRING_DEPARTMENT);
    }

    @PutMapping("/approve/{id}")
    public String approveRecruiter(
            @PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User Not Found"));

        user.setApproved(true);

        userRepository.save(user);

        return "Recruiter Approved Successfully";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(
            @PathVariable Long id) {

        userRepository.deleteById(id);

        return "User Deleted Successfully";
    }

    @PutMapping("/block/{id}")
    public String blockUser(
            @PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User Not Found"));

        user.setStatus("BLOCKED");

        userRepository.save(user);

        return "User Blocked Successfully";
    }

    @PutMapping("/activate/{id}")
    public String activateUser(
            @PathVariable Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(
                        () -> new RuntimeException(
                                "User Not Found"));

        user.setStatus("ACTIVE");

        userRepository.save(user);

        return "User Activated Successfully";
    }

    @GetMapping("/stats")
    public Map<String, Object> getStats() {

        Map<String, Object> stats =
                new HashMap<>();

        stats.put(
                "users",
                userRepository.count());

        stats.put(
                "recruiters",
                userRepository.countByRole(
                        Role.HIRING_DEPARTMENT));

        stats.put(
                "admins",
                userRepository.countByRole(
                        Role.ADMIN));

        stats.put(
                "pendingRecruiters",
                userRepository
                        .findByRoleAndApprovedFalse(
                                Role.HIRING_DEPARTMENT)
                        .size());

        return stats;
    }
}