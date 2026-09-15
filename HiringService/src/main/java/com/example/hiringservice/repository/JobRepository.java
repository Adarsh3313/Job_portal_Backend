package com.example.hiringservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hiringservice.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

    List<Job> findByLocation(String location);

    List<Job> findByTitleContainingIgnoreCase(String title);
}