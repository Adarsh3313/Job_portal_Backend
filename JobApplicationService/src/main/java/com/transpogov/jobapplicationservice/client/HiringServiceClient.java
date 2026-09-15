package com.transpogov.jobapplicationservice.client;

import com.transpogov.jobapplicationservice.dto.JobDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * Talks to HiringService via Eureka's service registry (name-based, load-balanced).
 * "hiring-service" must match spring.application.name in HiringService's own application.yml.
 * Adjust the path below to match HiringService's actual exposed endpoint.
 */
@FeignClient(name = "hiring-service")
public interface HiringServiceClient {

    @GetMapping("/api/jobs/{jobId}")
    JobDTO getJobById(@PathVariable("jobId") Long jobId);
}
