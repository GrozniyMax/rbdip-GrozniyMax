# NESSY.md — rbdip-bookstore

Instructional context for AI agents working in this repository.

## Project Overview

**rbdip-bookstore** is an educational project for the course «Рефакторинг баз данных и
приложений» (Refactoring Databases and Applications). It is a deliberately flawed book
store (e-commerce) application that students progressively improve across **5 lab
assignments**. The codebase and DB schema contain **intentional code smells and
anti-patterns** — these are the assignments, not bugs.

- **Language/Runtime:** Java 21 (no Kotlin in `src` — one earlier `DtoMapper.kt` was removed)
- **Build tool:** Maven (Spring Boot parent `3.3.4`), artifact `com.rbdip:bookstore:0.1.0`
- **Stack:** Spring Boot (web, data-jpa, validation) + Spring Data JPA (Hibernate) +
  PostgreSQL + Flyway + Testcontainers + ArchUnit
- **Grading:** fully automated via GitHub Actions CI; no manual review. Gates are aggregated
  by `scripts/write_score.py` into `score.json`. **Pass = total ≥ 60/100.**

### Lab structure (score gates → weight)

| Lab | Work | Gate | Points |
|---|---|---|---|
| L1 | Fix code smells; split the order God-class by SRP; remove magic numbers | `lab1_style` (Checkstyle + PMD no regression; reference tests green) | 15 |
| L2 | Characterization tests for `PricingCalculator`; normalize schema | `lab2_coverage` (JaCoCo ≥ 60%), `lab2_schema` | 20 |
| L3 | Zero-downtime schema evolution (expand-contract) + load test | `lab3_migration` | 15 |
| L4 | Fix N+1 in `GET /orders`; extract `review` module (Strangler Fig) | `lab4_nplusone`, `lab4_architecture` (ArchUnit) | 20 |
| L5 | Full pipeline green at once + mutation testing | `lab5_mutation` (PIT ≥ 60), `lab5_reference` | 30 |

## Building, Running, Testing

```bash
docker compose up -d                    # PostgreSQL 16 for manual run (bookstore/bookstore)
mvn spring-boot:run                     # app on http://localhost:8080

mvn verify                              # checkstyle + pmd + tests + jacoco (needs Docker)
mvn org.pitest:pitest-maven:mutationCoverage   # PIT mutation testing (separate, slow step)
python3 scripts/write_score.py          # aggregate target/ reports into score.json
```

- **Maven is not on PATH.** Use the wrapper distribution:
  `$HOME/.m2/wrapper/dists/apache-maven-3.9.11-bin/6mqf5t809d9geo83kj4ttckcbc/apache-maven-3.9.11/bin/mvn`.
  There is no `mvnw` script in the repo root.
- **Tests use Testcontainers** (PostgreSQL `16-alpine`) with their own isolated DB — Docker
  must be running; `docker compose up` is not needed for tests.
- **All quality gates report rather than fail the build:** `maven-surefire-plugin`
  `testFailureIgnore=true`; checkstyle/PMD/JaCoCo `failOnViolation=false`/`haltOnFailure=false`.
  Judge success by `scripts/write_score.py` / report files, **not** by `mvn verify` exit code.
- PIT targets only `com.rbdip.bookstore.order.service.PricingCalculator`, threshold `60`.

## Architecture & Source Layout

Layered per module: **controller (`dto/in` + `dto/out`) → service → repository (`dao` + `entities`)**.
Shared base in `com.rbdip.bookstore.abstracts`.

```
src/main/java/com/rbdip/bookstore/
  BookstoreApplication.java, GlobalExceptionHandler.java
  abstracts/
    AbstractDao.java        — abstract DAO base over JpaRepository; DOMAIN ↔ ENTITY via Mapper
    Mapper.java             — interface: TO map(FROM), FROM unmap(TO)
    ItemNotFountException   — base not-found exception (extends IllegalArgumentException)
  order/
    controller/DtoMapper.java, OrderController.java
    controller/dto/in/  CreateOrderRequest, CustomerDto
    controller/dto/out/ OrderCreationResult, OrderDto, OrderItemDto
    repository/         OrderRepository, OrderItemRepository (Spring Data)
    repository/dao/     OrderDao, OrderItemDao, OrderMapper, OrderItemMapper
    repository/entities/ Order, OrderItem (JPA)
    service/            OrderCreator, OrderStorage, OrderNotifier, PricingCalculator
    service/domain/     Order, OrderItem, OrderStatus, CouponCodes
    service/domain/customer/   Customer, CustomerType
    service/domain/discount/   Discount, PercentDiscount, AmountDiscount
  product/
    controller/dto/in/ CreateProductRequest ; controller/dto/out/ ProductDto
    domain/            Product, ProductNotFountException   ← NOTE: product/domain (not service/domain)
    repository/        ProductRepository
    repository/dao/    ProductDao, ProductMapper
    repository/entities/ Product (JPA)
  review/
    controller/dto/in/ CreateReviewRequest ; controller/dto/out/ ReviewCreationResult, ReviewDto
    repository/        ReviewRepository
    repository/dao/    ReviewDao, ReviewMapper
    repository/entities/ Review (JPA)
    service/ReviewService ; service/domain/ Review
src/main/resources/db/migration/   V1__init.sql (DO NOT EDIT) + student V2, V3, ...
src/test/java/com/rbdip/bookstore/reference/   — reference tests (DO NOT EDIT)
scripts/write_score.py            — aggregates reports into score.json
```

### Current state (branch `lab1`, refactor IN PROGRESS)

The `order`/`product`/`review` modules have been restructured into the layered pattern above,
and `OrderCreator` was split by SRP into `OrderCreator` + `OrderStorage` + `OrderNotifier` +
`PricingCalculator`. **Important inconsistencies / breakages that are NOT yet resolved:**

1. **The reference tests currently do NOT compile against the refactored code.** They encode
   the *original* public contract and must not be edited, so the API must be reconciled:
   - `OrderApiReferenceTest` / `NPlusOneReferenceTest` build `CreateOrderRequest` with the old
     6-arg shape `(customerName, customerAddress, customerPhone, customerType, couponCode,
     items)`. The record is now `(CustomerDto customer, List<Item> items, String couponCode)`.
   - `PricingCalculatorReferenceTest` calls `calculateOrderTotal(List<LineItem>, String,
     String)`; the method is now `(List<OrderItem>, Customer, String)` and `LineItem` no longer
     exists.
2. `OrderController.createOrder` has **no `@Valid`** on `@RequestBody`, so the `@NotEmpty
   items` constraint never triggers — `rejectsOrderWithoutItems` (expects HTTP 400) would fail.
3. **Package-convention inconsistency:** `product` uses `product.domain` for its domain model,
   while `order`/`review` use `<module>.service.domain`. Align this when convenient.
4. Untracked new files exist under `review/` (`dto/`, `dao/`, `service/domain/`) — not yet staged.

Keep these in mind before claiming any gate passes; the test module currently fails to compile.

## The `reference` Test Package (CRITICAL)

Everything under `src/test/java/com/rbdip/bookstore/reference/` — including `architecture/` —
contains the **instructor's reference tests and architectural rules**. **NEVER edit, move,
rename, or "fix" these files.** They are either already-green-and-must-stay-green, or
intentionally-red-and-are-the-assignment (`SchemaNormalizationReferenceTest`,
`ArchitectureRulesTest`, `NPlusOneReferenceTest`, `PricingCalculatorReferenceTest`'s untested
branches).

- `OrderApiReferenceTest` asserts the public order API (status `"new"`, `id`, `items`,
  `customerFullName`).
- `NPlusOneReferenceTest` is the L4 gate: `GET /orders` must not issue a per-order query.
- `ArchitectureRulesTest` (L4): `com.rbdip.bookstore.review..` must not depend on
  `com.rbdip.bookstore.order..` — currently red by design.
- `PricingCalculatorReferenceTest` covers only the happy path; the uncovered branches are the
  L2 characterization-test assignment.

Reference tests extend `AbstractIntegrationTest` (Testcontainers singleton Postgres,
`TestRestTemplate` + `JdbcTemplate`, truncates tables before each test).

## Development Conventions

- **DAOs:** extend `AbstractDao<DOMAIN, ENTITY, ID>` (base holds `protected` `repository` +
  `mapper`). Concrete DAOs inject the concrete Spring Data repository (pass it to `super`,
  keep a typed field for custom queries) and map results with `mapper::unmap`. Add custom
  query methods on the DAO, not the repository. Example: `OrderItemDao.findByOrderId`.
- **Mappers:** `@Component implements Mapper<DOMAIN, ENTITY>`; `map(domain)→entity`,
  `unmap(entity)→domain`. Where entity and domain share a name (`Order`, `Review`, `Product`),
  import the domain and fully-qualify the entity (as in `OrderMapper`, `ReviewMapper`).
- **Domain model:** Java records (`Order`, `OrderItem`, `Product`, `Review`, `Customer`).
  Enums with code + reverse lookup: `CustomerType.withCode`, `OrderStatus.withValue`,
  `CouponCodes.withCode`.
- **Discounts:** `Discount` interface (`apply(BigDecimal)` + static `Zero()`);
  `PercentDiscount(multiplier)`, `AmountDiscount(amount)`; customer discount via
  `Customer.getCustomerDiscount()`.
- **DTOs:** records in `dto/in` (request, jakarta validation) and `dto/out` (response).
  Controllers return typed DTOs, **not** `Map`.
- **Entities:** `protected` no-arg constructor (`// for JPA`) + full constructor + getters.
  Persistence config: `ddl-auto: validate` (Flyway owns schema), `open-in-view: false`,
  Hibernate statistics enabled.
- **Error handling:** throw `IllegalArgumentException` (or `*NotFountException` subclasses) for
  validation/not-found; `GlobalExceptionHandler` maps to HTTP 400. Do not scatter handling.
- **Transactions:** `@Transactional` on service methods (`OrderStorage.createOrder`).
- **Intentional smells to preserve (they are the labs):** N+1 in `OrderStorage.findAll` (L4);
  `ReviewService` reaching into `order` repositories (L4); untested `PricingCalculator`
  discount branches (L2/L5).
- **Comments:** concise Russian Javadoc explaining *why* something is a smell and which lab it
  targets. Match this tone.

### Quality gates (from `pom.xml`)

- Checkstyle `3.5.0` (`checkstyle.xml`, severity `warning`, `failOnViolation=false`).
- PMD `3.24.0` rulesets: `bestpractices`, `design`, `errorprone` (`failOnViolation=false`).
- JaCoCo `0.8.12`: LINE coverage min `0.60` (`haltOnFailure=false`).
- PIT `1.17.3`: `mutationThreshold=60`, targets `PricingCalculator`.
- Surefire: `testFailureIgnore=true`.
- `scripts/write_score.py` reads these reports into `score.json`.

## Critical Rules for AI Agents

1. **Never modify** anything under `src/test/java/com/rbdip/bookstore/reference/`.
2. Keep green reference tests green; treat intentionally-red ones as the goal — never weaken
   them to pass.
3. Respect the L4 architecture rule: `review` must not depend on `order` internals.
4. `pom.xml` gates report rather than fail — judge by `scripts/write_score.py`, not exit codes.
5. Use real Docker/Testcontainers blackbox tests matching the reference-test style.
6. Run verification (`mvn verify`, PIT, `python3 scripts/write_score.py`) before claiming a
   gate passes — and note that the test module currently does not compile (see Current state).
7. Do not reintroduce Kotlin/`Map`-based controllers; keep the layered domain/DAO/DTO pattern.