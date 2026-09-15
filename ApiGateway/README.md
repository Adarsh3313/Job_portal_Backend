# ApiGateway

Single entry point for all `CTS-TranspoGovMicroservicesAPI` microservices, built with Spring Cloud Gateway.

## Stack
- Java 17, Spring Boot 3.2.5, Spring Cloud 2023.0.1
- Spring Cloud Gateway (reactive — do **not** add `spring-boot-starter-web` to this project, it conflicts
  with Gateway's Netty/WebFlux stack and is a common cause of startup failures)
- Eureka discovery client
- Spring Cloud Config client

Default port: **8080**

## Routes configured (edit in `application.yml` if your service names/paths differ)

| Route ID | Path prefix | Forwards to (Eureka service name) |
|---|---|---|
| authentication-service | `/api/auth/**` | `authentication-service` |
| company-service | `/api/companies/**` | `company-service` |
| hiring-service | `/api/jobs/**` | `hiring-service` |
| interview-service | `/api/interviews/**` | `interview-service` |
| job-application-service | `/api/applications/**` | `job-application-service` |
| notification-service | `/api/notifications/**` | `notification-service` |
| resume-service | `/api/resumes/**` | `resume-service` |

> **Important:** each route's `uri: lb://<name>` must exactly match the `spring.application.name`
> that service registers under in Eureka. If your services use different names (check each service's
> own `application.yml`), update the `uri` values here to match — otherwise Gateway will return
> `503 Service Unavailable` because it can't find that name in the registry.

`spring.cloud.gateway.discovery.locator.enabled: true` is also turned on, so every registered service
is additionally reachable at `/SERVICE-NAME/**` automatically, even without an explicit route — handy
for quick testing.

## Before you run it
1. Start **EurekaRegistry** first (default `http://localhost:8761`).
2. Start **ConfigServer** next (default `http://localhost:8888`) if you're using it.
3. Then start the backend services (AuthenticationService, CompanyService, HiringService,
   InterviewService, JobApplicationService, NotificationService, ResumeService).
4. Start **ApiGateway** last.

## Fixing "Could not find or load main class ..." errors
If you see an error like:
```
Could not find or load main class com.example.demo.SomeOtherApplication
Caused by: java.lang.ClassNotFoundException: ...
```
this is **not** a code problem — it means the IDE's Run Configuration for this project is pointing at
the wrong/stale main class (common in Eclipse/STS when a run config gets copied between projects, or
is left over from an old Spring Initializr scaffold). Fix:
1. **Run → Run Configurations…**
2. Find the entry being used for ApiGateway, open the **Main** tab.
3. Confirm **Project** = `ApiGateway` and **Main class** = `com.transpogov.apigateway.ApiGatewayApplication`.
4. If wrong, fix it or delete the run config entirely, then right-click the project →
   **Run As → Spring Boot App** to let STS regenerate a correct one.

## Sample request through the gateway
```bash
curl http://localhost:8080/api/applications/1
```
This gets routed by ApiGateway to whichever `job-application-service` instance is registered in Eureka.

## Test connectivity
```bash
curl http://localhost:8080/actuator/gateway/routes
```
Lists every route Gateway currently has loaded — useful to confirm your routes registered correctly.
