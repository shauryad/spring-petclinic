---
name: testing-petclinic
description: End-to-end testing for Spring PetClinic application. Use when verifying UI, JPA persistence, validation, or actuator endpoints after code changes.
---

# Testing Spring PetClinic

## Prerequisites

- Java 17+ installed
- Maven wrapper (`./mvnw`) available in repo root
- No external database needed — app uses H2 in-memory DB by default
- No authentication required

## Starting the Application

```bash
cd /home/ubuntu/repos/spring-petclinic
./mvnw spring-boot:run
```

The app starts on `http://localhost:8080`. First startup may take 30-60 seconds due to Maven compilation.

Verify startup by checking:
```bash
curl -s http://localhost:8080/actuator/health
# Expected: {"status":"UP"}
```

## Key Endpoints to Test

| Endpoint | Method | What It Tests |
|----------|--------|---------------|
| `/` | GET | Welcome page — Thymeleaf rendering, Spring context |
| `/owners/find` | GET | Find owners search form |
| `/owners?lastName=Franklin` | GET | JPA query — should redirect to `/owners/1` (single result) |
| `/owners?lastName=Davis` | GET | JPA query — should show owners list (multiple results) |
| `/owners/1` | GET | Owner detail with pets/visits — JPA entity relationships |
| `/owners/new` | GET/POST | Create owner — jakarta.validation (@NotEmpty, @Digits) |
| `/owners/1/pets/new` | GET/POST | Add pet — JPA cascade, pet type dropdown |
| `/owners/1/pets/1/visits/new` | GET/POST | Add visit — JPA cascade persistence |
| `/vets.html` | GET | Vet list HTML with specialties, pagination |
| `/vets` | GET | Vet list XML/JSON serialization (jakarta.xml.bind) |
| `/actuator/health` | GET | Spring Boot actuator health check |

## Preloaded Test Data

The H2 database is populated from `src/main/resources/db/h2/data.sql`:
- 10 owners (George Franklin=ID 1, Betty Davis=ID 2, etc.)
- 13 pets across the owners
- 6 veterinarians with specialties (radiology, surgery, dentistry)
- 4 visits

## Validation Testing

To test jakarta.validation:
1. Go to `/owners/new`
2. Submit with empty fields → expect "must not be empty" errors on all 5 fields
3. Telephone field also shows @Digits constraint error
4. Fill valid data and submit → should redirect to new owner's detail page

Owner validation constraints:
- `firstName`: @NotEmpty
- `lastName`: @NotEmpty
- `address`: @NotEmpty
- `city`: @NotEmpty
- `telephone`: @NotEmpty + @Digits(fraction=0, integer=10)

## Running Unit Tests

```bash
./mvnw test          # Run all tests
./mvnw verify        # Run tests + generate Jacoco coverage report
```

Coverage report: `target/site/jacoco/jacoco.csv`

## Common Issues

- If the app fails to start, check that no other process is using port 8080
- The date input field uses HTML5 date picker — type dates in MM/DD/YYYY format in the browser
- The `/vets` endpoint returns XML by default in browser (no Accept header); use `curl -H 'Accept: application/json'` for JSON
- Spring Boot 3.x requires Java 17+; verify with `java -version`

## Devin Secrets Needed

None — the application runs entirely locally with an embedded H2 database and no authentication.
