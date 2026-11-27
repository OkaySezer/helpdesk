# IT Help Desk and Service Request Workflow

Software Engineering semester project (Topic 4). A university IT help-desk system:
users open tickets, tickets are classified, assigned to technicians, move through a
controlled lifecycle, and a supervisor sees a basic service dashboard.

See [CLAUDE.md](CLAUDE.md) for the full project rules, team method (Kanban), TDD rules,
Git workflow and the evidence checklist.

## Tech stack

Java 21 · Spring Boot 4.2 · Maven (wrapper) · JUnit 5 · JaCoCo · PostgreSQL ·
Testcontainers · Docker Compose · GitHub Actions.

Base package: `edu.mcbu.helpdesk`

## Build and test

```bash
./mvnw test       # build + run tests            (Windows: mvnw.cmd test)
./mvnw verify     # + JaCoCo coverage report  -> target/site/jacoco/index.html
```

- Integration tests use Testcontainers and are **skipped if Docker is not running**.
  On CI (GitHub Actions) Docker is available, so they actually run there.

## Run locally

```bash
./mvnw spring-boot:run   # compose.yaml starts PostgreSQL automatically (Docker required)
```

- Health check: `GET /actuator/health`

## Continuous integration

Every push and pull request triggers [.github/workflows/ci.yml](.github/workflows/ci.yml),
which builds the project and runs all tests (with coverage) on an Ubuntu runner.
