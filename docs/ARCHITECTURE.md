# Loan API architecture

The Loan API is currently one Spring Boot application. It accepts HTTP requests, applies loan rules, and stores loans in H2 through Spring Data JPA. This diagram describes the current code, not the future Loan Platform.

## Successful request and response

```text
REQUEST
Client
  │ HTTP request + JSON
  ▼
Spring MVC / DispatcherServlet
  │ route request; JSON → LoanRequest; @Valid on POST
  ▼
LoanController
  │ LoanRequest DTO
  ▼
LoanService
  │ Loan entity
  ▼
LoanRepository
  │ JPA/Hibernate SQL
  ▼
H2 database

RESPONSE
H2 database
  │ saved row / generated ID
  ▼
LoanRepository
  │ Loan entity
  ▼
LoanService
  │ maps entity → LoanResponse DTO
  ▼
LoanController
  │ Spring MVC converts LoanResponse → HTTP/JSON
  ▼
Client
```

`LoanRequest` and `LoanResponse` are **DTOs**, not extra processing layers. They describe what crosses the API boundary. `Loan` is the JPA **entity** used for persistence; the service maps between DTOs and the entity. The controller handles HTTP routing and status codes, the service handles loan rules, and the repository provides database operations. Spring creates and injects the controller, service, and repository beans.

For `POST /api/loans`, the service rejects amounts over €100,000. Otherwise, it assigns `APPROVED` through €25,000 or `MANUAL_REVIEW` above that, saves the entity, and returns a `LoanResponse` with its generated ID. The controller returns HTTP 201 for successful creation.

## Error path

```text
Exception from validation or service
  → Spring MVC exception resolution
  → matching @ExceptionHandler in GlobalExceptionHandler
  → HTTP error response
```

| Origin | Exception | Response from `GlobalExceptionHandler` |
| --- | --- | --- |
| Invalid `POST` fields (`@Valid`, before the controller method runs) | `MethodArgumentNotValidException` | 400; JSON map of field names to messages |
| Service rejects an amount over €100,000 | `LoanAmountExceededException` | 400; text message |
| Service cannot find a loan ID | `LoanNotFoundException` | 404; text message |

`@ExceptionHandler` is an annotation on methods **inside** `GlobalExceptionHandler`; it is not a separate layer. Spring MVC selects the matching method when an exception occurs. Exceptions not matched by this advice use Spring's default error handling, so this app does not yet define one consistent format for every error.

## Current boundaries

The database is H2, not PostgreSQL. The status-count method exists only in the service and has no HTTP endpoint. `POST` uses `@Valid`; the current `PUT` method does not. Authentication, reviewer decisions, messaging, and deployment are future work, not part of this diagram.
