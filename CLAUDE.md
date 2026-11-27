# CLAUDE.md — IT Help Desk (Software Engineering Semester Project, Fall 2026)

Guidance for Claude Code and for the team. All project artifacts (reports, requirements,
task descriptions, commit messages, presentations) must be in **English**.

## 1. Project in one paragraph

Topic 4 — **IT Help Desk and Service Request Workflow**. Client: a university IT unit
(simulated client; role-player must be clearly identified, never present invented meetings
as real). Users open **tickets**, tickets are classified, assigned to technicians, move
through a controlled lifecycle and are resolved/closed; a supervisor sees a basic service
dashboard. The grade rewards **process evidence** (Kanban, TDD, CI, requirements,
reviews), not feature count — the product itself is only 5%.

### MVP (core capabilities)
1. Ticket creation
2. Classification (category, priority)
3. Assignment to a technician
4. Status transitions (lifecycle)
5. Basic service dashboard

### Key business rules (the TDD core)
- **Transition permissions** — who may move a ticket to which state (e.g. a requester cannot mark it Resolved).
- **Resolution criteria** — a ticket cannot become Resolved without a resolution note.
- **Reopening limit** — a closed ticket can be reopened at most N times.
- **Response deadlines (SLA)** — e.g. high priority must get a first response within X hours.

### Expected change challenge
Priority-based **escalation policy** (e.g. overdue high-priority tickets escalate to a supervisor).
Must be handled as a tracked requirement change (change log → revised acceptance criteria →
revised tests → implementation → regression results).

### Deferred (out of scope)
Automatic machine diagnosis, real e-mail integration.

### Draft ticket lifecycle (state diagram)
`New → Assigned → In Progress → Resolved → Closed`, plus `Resolved/Closed → Reopened → In Progress` (limited).

### Stakeholders (≥ 3)
Requester (student/staff), technician / help-desk agent, IT supervisor, and at least one
affected non-user (e.g. a department office that depends on resolved outages).

## 2. Team & method

- Team of **3**, method **Kanban** (no artificial sprints or velocity).
- Each member leads an area but **everyone** does requirements, tests (incl. ≥1 TDD cycle),
  implementation and reviews. No permanently isolated modules.
  - A: requirements, SRS, UML, change log
  - B: CI pipeline, Docker, deployment, rollback
  - C: Jira board, flow metrics, risk register, defect log

### Board & policies
- Columns: `Backlog → Ready → In Progress → Review → Done`
- WIP limits: **In Progress ≤ 3**, **Review ≤ 2**. If Review is full, review before starting new work. Record any WIP breach and why.
- **Ready** entry: has ID, clear statement, acceptance criteria, small enough for ≤ 2 days.
- **Done** = Definition of Done met (see below).
- Blocked items: flag on the card with reason and date; discuss at next sync.
- Cycle time is measured from **entering In Progress** to **entering Done**.
- Track WIP, throughput (items Done per week), cycle time, work item age. Analyze ≥ 1 bottleneck and the action taken.
- Two short syncs per week; keep brief notes (improvement discussions are graded evidence).

### Definition of Done
Acceptance criteria met · relevant tests passing · reviewed by another member · CI green ·
required docs updated · no unresolved critical defect.

## 3. Tech stack & commands

Java 21 · Spring Boot 4.2 · Maven (wrapper) · JUnit 5 · JaCoCo · PostgreSQL · Testcontainers ·
Docker Compose · GitHub Actions (or Jenkins if required) · Jira.

```bash
./mvnw test          # build + run tests          (Windows: mvnw.cmd test)
./mvnw verify        # + JaCoCo coverage report  -> target/site/jacoco/index.html
./mvnw spring-boot:run   # run locally; compose.yaml starts PostgreSQL automatically (Docker required)
```
- Health check: `GET /actuator/health`
- Integration tests use Testcontainers and are **skipped if Docker is not running**.
- Base package: `edu.mcbu.helpdesk`

### Coding conventions
- **Never call `LocalDateTime.now()` / `Instant.now()` directly in domain code.** Inject
  `java.time.Clock`; tests use `Clock.fixed(...)` so SLA/deadline boundaries are testable.
- Keep business rules in plain domain/service classes (unit-testable without Spring).
- Synthetic data only. Never commit credentials or real personal data.

## 4. Git workflow

- `main` is protected: no direct pushes; merge requires green CI + ≥ 1 approval from another member.
- One short-lived branch per Jira item: `HD-<id>-short-description` (e.g. `HD-7-reopen-limit`).
- Every commit message starts with the item ID: `HD-7 ...`.
- Every PR is reviewed by another member; comments should address behavior, requirements,
  tests or maintainability (plain "LGTM" is weak evidence). For paired work, the third member reviews.
- Tag releases (`v0.1.0`, ...) and link them to CI runs.

## 5. TDD rules (mandatory for business rules)

- Red → Green → Refactor in small cycles. **Write the failing test first, run it, see it fail
  for the right reason** (missing behavior, not a compile/environment error).
- Commit each step separately so the evidence is attributable:
  `HD-7 red: ...`, `HD-7 green: ...`, `HD-7 refactor: ...`. Do not squash these away
  (keep an evidence tag/branch if squashing).
- Document ≥ **5** representative cycles in `docs/tdd/` (each member ≥ 1): item ID, test
  description, Red revision + failure output, Green revision + passing output, Refactor revision or note.
- Claude Code: when implementing a rule, **write the test first and stop for the Red run**;
  do not write implementation and tests together.

## 6. Minimum evidence checklist

| Item | Minimum |
|---|---|
| User / system / measurable NFR requirements | 6 / 12 / 4 |
| Form-based specs / decision table | 2 / 1 |
| Detailed scenarios | 3 |
| UML | use case, class, 2 sequence (one failure path), state (ticket) |
| TDD cycles | ≥ 5, every member ≥ 1 |
| Integration tests / acceptance scenarios | 3 / 3 |
| Core-logic line coverage | ≥ 80% (state scope & exclusions) |
| Controlled fault-injection check | 1 |
| Requirement changes | 2 (one changes an implemented rule) |
| Genuine (or labeled) failed CI run + fix | 1 |
| Analyzed defects | 3 |
| Process improvements with before/after | 2 |
| Elicitation techniques | ≥ 2 (one with direct stakeholder interaction) |

Also: risk register, change log, defect log, independent setup attempt, rollback/recovery
demo, evidence index, final report, ~15 min demo + individual defense.

## 7. Four-week plan (confirm checkpoint dates with the instructor)

1. **Week 1** — repo, Jira, CI; client & stakeholders; elicitation; first requirements +
   acceptance criteria; DoD; first TDD cycle (ticket creation).
2. **Week 2** — lifecycle & permissions via TDD; assignment; SRS draft + UML; stakeholder
   review; interim report (date to confirm).
3. **Week 3** — escalation change + regression tests; SLA/deadline rules with controlled
   clock; Docker + test deployment; dashboard; process improvement #1.
4. **Week 4** — integration & acceptance tests, coverage, fault injection; rollback rehearsal;
   independent setup; improvement #2; defect log; evidence index, final report, demo.

CI runs from week 1. Do not postpone tests, Docker or docs to the last week.

## 8. Planned repository layout

```
docs/
  requirements/   SRS, requirement table, change log
  uml/            diagrams
  tdd/            TDD cycle evidence
  process/        board policies, flow metrics, sync notes, risk register
  defects/        defect log
  ai-usage.md     record of AI assistance
src/main/java/edu/mcbu/helpdesk/
src/test/java/edu/mcbu/helpdesk/
compose.yaml, Dockerfile, CI pipeline file
```

## 9. Responsible use of AI

Record in `docs/ai-usage.md` the purpose and important outputs adopted from AI tools, and
any substantial corrections. Never fabricate client meetings, commits, TDD failures, reviews
or pipeline runs. Every member must be able to explain all submitted work.
