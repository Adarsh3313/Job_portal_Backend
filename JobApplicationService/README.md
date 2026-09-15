# JobApplicationService

Fills the gap in the `CTS-TranspoGovMicroservicesAPI` microservices set. Candidates apply to jobs
posted by **HiringService**, and status changes trigger notifications via **NotificationService**.

## Stack
- Java 17, Spring Boot 3.2.5, Spring Cloud 2023.0.1
- Spring Data JPA + MySQL
- Eureka discovery client (registers with your existing `EurekaRegistry`)
- Spring Cloud Config client (pulls from your existing `ConfigServer`)
- OpenFeign (calls `hiring-service` and `notification-service` by name through Eureka)

## Before you run it

1. **Create the DB** (or let `createDatabaseIfNotExist=true` in `application.yml` do it) and set
   real credentials in `src/main/resources/application.yml` under `spring.datasource`.
2. **Confirm service names.** Feign calls other services by their `spring.application.name`.
   This project assumes:
   - HiringService's `spring.application.name: hiring-service`
   - NotificationService's `spring.application.name: notification-service`
   If your services are registered under different names in Eureka, update the `@FeignClient(name = "...")`
   values in `client/HiringServiceClient.java` and `client/NotificationServiceClient.java` to match.
3. **Confirm endpoint shapes.** This project guesses:
   - `GET /api/jobs/{jobId}` on HiringService, returning `{id, title, companyName, location, active}`
   - `POST /api/notifications` on NotificationService, accepting `{recipientEmail, recipientName, subject, message}`
   If your real endpoints/fields differ, update `JobDTO`, `NotificationRequestDTO`, and the two Feign
   client interfaces accordingly — everything else in the service is decoupled from those details.
4. Make sure **ConfigServer** and **EurekaRegistry** are already running (per your screenshot, ports
   default to `8888` and `8761` respectively — adjust `application.yml` if yours differ).

Default port: **8087** (change under `server.port` if it collides with another service).

## Endpoints

| Method | Path | Purpose |
|---|---|---|
| POST | `/api/applications` | Candidate applies to a job |
| GET | `/api/applications` | List all applications |
| GET | `/api/applications/{id}` | Get one application |
| GET | `/api/applications/candidate/{candidateId}` | All applications by a candidate |
| GET | `/api/applications/job/{jobId}` | All applications for a job |
| PATCH | `/api/applications/{id}/status` | Update status (triggers notification) |
| PATCH | `/api/applications/{id}/withdraw` | Candidate withdraws application |
| DELETE | `/api/applications/{id}` | Delete an application |

## Sample requests

Apply:
```bash
curl -X POST http://localhost:8087/api/applications \
  -H "Content-Type: application/json" \
  -d '{
    "jobId": 1,
    "candidateId": 10,
    "candidateName": "Jane Doe",
    "candidateEmail": "jane@example.com",
    "resumeId": 5,
    "coverLetter": "I would love to work here."
  }'
```

Update status:
```bash
curl -X PATCH http://localhost:8087/api/applications/1/status \
  -H "Content-Type: application/json" \
  -d '{"status": "INTERVIEW_SCHEDULED", "remarks": "Interview on Friday 10am"}'
```

## Design notes
- Feign calls to HiringService and NotificationService are **wrapped defensively**: if HiringService
  is briefly down, the application still gets recorded (job existence check is best-effort, logged as
  a warning); if NotificationService is down, the workflow still completes — notifications are
  fire-and-forget so a flaky downstream service never blocks a candidate's application.
- A candidate can't apply twice to the same job (`existsByJobIdAndCandidateId` check).
- `ApplicationStatus` enum: `APPLIED -> UNDER_REVIEW -> SHORTLISTED -> INTERVIEW_SCHEDULED -> HIRED/REJECTED`,
  plus `WITHDRAWN` for candidate-initiated withdrawal.

## Wire it into ApiGateway
Add a route for `job-application-service` in your `ApiGateway` config (application.yml or
ConfigServer-hosted config repo) the same way you did for the other services, e.g.:
```yaml
- id: job-application-service
  uri: lb://job-application-service
  predicates:
    - Path=/api/applications/**
```
