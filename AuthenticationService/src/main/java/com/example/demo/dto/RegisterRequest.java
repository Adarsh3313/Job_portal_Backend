package com.example.demo.dto;

import com.example.demo.enums.Role;

import lombok.Data;

@Data
public class RegisterRequest {

    private String fullName;

    private String email;

    private String password;

    private String phone;

    private Role role;

}
