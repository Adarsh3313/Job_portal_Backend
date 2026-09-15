# AuthenticationService — Postman tests

29 requests across 7 folders, covering every endpoint in the service.

## Import & run

1. Postman → **Import** → `AuthenticationService.postman_collection.json`
2. Start the service (`mvn -f AuthenticationService/pom.xml spring-boot:run`) — it must be reachable on `http://localhost:8083`.
3. Open the **Collection Runner**, select the whole collection, **Run**.

Run it **in order**. The folders form one flow: register → login → admin approves → block/activate/delete. Later requests read ids and tokens captured by earlier ones.

Each run generates a 7-digit `runId` and derives fresh emails from it (`qa.user.<runId>@example.com`), so the suite is re-runnable without cleaning the database. The seeded USER is deleted at the end of folder 04; the recruiter and admin rows are left behind on purpose so you have a working admin login.

Only two variables are worth editing:

| Variable | Default | Notes |
|---|---|---|
| `baseUrl` | `http://localhost:8083` | Point at the API Gateway instead if you want to test through it |
| `password` | `Admin@123` | Used for every seeded account |

## What's covered

| Folder | Checks |
|---|---|
| 00 — Smoke | `/test` banner, `/actuator/health` including the `db` component |
| 01 — Register | USER / HIRING_DEPARTMENT / ADMIN paths, duplicate-email rejection, the pending-approval message |
| 02 — Login | Token issued; JWT decoded and its `sub`, `role` and 24h `exp` asserted; wrong password, unknown email, and unapproved-recruiter rejections |
| 03 — Admin approval | `/stats` counters, `/pending-recruiters` filtering, approve, recruiter can then log in, approve on a missing id |
| 04 — Admin lifecycle | Block → login refused → activate → login works → delete → login refused |
| 05 — Role endpoints | `/user` and `/hiring` with the matching tokens |
| 06 — Security gaps | See below |

Two things to know about the assertions:

- **Errors are `400`, not `401`/`403`.** `GlobalExceptionHandler` maps every `RuntimeException` to `400` with a `{timestamp, status, error}` body, so "Invalid Credentials" comes back as a 400. The tests match that.
- **Register/approve/block responses are plain text**, not JSON — those tests use `pm.response.text()`.

## Folder 06 — the security gaps

These four tests assert **what the service does today**, not what it should do, so the suite runs green. Each one logs a `FINDING:` line to the Postman console. They're real issues:

1. **Every endpoint is public.** `SecurityConfig` ends with `anyRequest().permitAll()`, so `/api/admin/**` answers anonymous callers — no token needed to list, block, or delete users.
2. **`/api/admin/users` returns BCrypt hashes.** `AdminController` serialises the `User` entity directly, so every row ships its `password`. Needs a DTO or `@JsonIgnore`.
3. **`@PreAuthorize` never runs.** There's no `@EnableMethodSecurity` on the application class, so the role checks on `/user` and `/hiring` are inert annotations — an ADMIN token passes the `hasRole('USER')` check.
4. **Invalid tokens aren't rejected.** `JwtAuthenticationFilter` catches parse failures and calls `doFilter` anyway, so a forged token behaves exactly like no token.

Two more worth noting, not currently asserted:

- **`/api/auth/register` trusts the client's `role`.** Anyone can POST `"role":"ADMIN"` and get an approved admin account. The collection relies on this to create its admin — which is itself the demonstration.
- **`JwtService` ignores `jwt.secret`.** The signing key is hardcoded at [JwtService.java:16](../src/main/java/com/example/demo/service/JwtService.java:16), so the value in `application.properties` is dead config and the real secret is in version control.

When you harden the service, folder 06 will start failing. That's the signal to flip those expectations to `401`/`403`.

## CI

```bash
newman run AuthenticationService/postman/AuthenticationService.postman_collection.json --env-var baseUrl=http://localhost:8083
```
