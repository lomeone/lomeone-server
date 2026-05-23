# AGENTS.md

This file provides guidance to Codex (Codex.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
./gradlew build                    # Build all modules
./gradlew test                     # Run all tests (JUnit Platform + Kotest)
./gradlew :domain:test             # Run tests for domain module
./gradlew :api:test                # Run tests for api module
./gradlew :infrastructure:test     # Run tests for infrastructure module
./gradlew koverHtmlReport          # Generate coverage HTML report
./gradlew koverXmlReport           # Generate coverage XML report (for CI)
```

**Environment requirements**: 
- Java 21 (JVM toolchain configured)
- `GITHUB_ACTOR` and `GITHUB_TOKEN` environment variables must be set to pull `eunoia` packages from GitHub Packages Maven registry

## Multi-Module Architecture

4개의 Gradle 서브모듈로 구성된 Kotlin/Spring Boot 서버 (Java 21, Kotlin 2.3.10)

### Module Dependency Graph

```
api (entry point)
├─> domain (business logic)
│   └─> util (shared utilities)
└─> infrastructure (external integrations)
    └─> domain
```

### api (Entry Point)
- **Entry Point**: `LomeoneApiApplication.kt`
- **Package**: `com.lomeone.texhol`
- **Dependencies**: domain, infrastructure
- **Tech Stack**: 
  - Spring Boot Web (REST API)
  - Netflix DGS (GraphQL)
  - SpringDoc OpenAPI (Swagger UI)
  - Spring Boot Actuator (observability)
  - Jib (container image builds)
  - eunoia-spring-web
- **Structure**:
  - `texhol/rest/api/` - REST controllers organized by domain
    - `reservation/` - 예약 API
    - `management/` - 관리 API
  - `texhol/graphql/` - GraphQL resolvers (DGS)
  - `config/` - API layer configuration

### domain (Business Logic)
- **Package**: `com.lomeone.texhol`
- **Dependencies**: util
- **Tech Stack**: 
  - Spring Data JPA
  - QueryDSL (KSP code generation) - type-safe queries
  - Kotlin allOpen plugin (for `@Entity`, `@MappedSuperclass`, `@Embeddable`)
  - eunoia-exception, eunoia-kotlin-util, eunoia-aws, eunoia-security
- **Bounded Contexts** (DDD style):
  - `reservation/` - 예약 관리
    - `entity/` - JPA entities
    - `service/` - use case services
    - `repository/` - repository interfaces
  - `game/` - 게임 세션, 엔트리, 바이인 기록
    - `entity/`, `service/`, `repository/`, `exception/`
  - `store/` - 매장 관리
    - `entity/`, `service/`, `repository/`, `exception/`
  - `player/` - 플레이어
    - `entity/`, `exception/`
  - `common/` - 공통 기반 클래스
    - `entity/AuditEntity` - created/updated timestamp tracking
    - `entity/Email` - value object

**QueryDSL**: Q-classes are auto-generated during build (KSP) and excluded from Kover coverage

### infrastructure (External Integrations)
- **Package**: `com.lomeone`
- **Dependencies**: domain
- **Tech Stack**: 
  - Spring Data JPA (repository implementations)
  - QueryDSL (for complex queries)
  - OpenFeign (HTTP client, web engine excluded)
  - AWS SDK (DynamoDB, STS)
  - AWS Smithy Kotlin (observability)
  - MySQL (production), H2 (test runtime)
  - OpenTelemetry (AWS SDK instrumentation)
- **Structure**:
  - `texhol/reservation/` - repository implementations
  - `user/repository/` - user data access
  - `config/` - infrastructure configuration
    - `datasource/` - database configuration
    - `aws/` - AWS service clients
    - `openfeign/` - Feign client configuration

### util (Shared Utilities)
- **Package**: `com.lomeone`
- **Dependencies**: none
- Shared utility classes (e.g., crypto converters)

## Key Libraries & Versions

Core dependencies (from `libs.versions.toml`):
- **Kotlin**: 2.3.10
- **Spring Boot**: 4.0.3
- **Spring Cloud**: 2025.1.1
- **QueryDSL**: 7.1 (KSP: 2.3.5)
- **Netflix DGS**: 11.1.0 (codegen: 8.3.0)
- **AWS SDK**: 2.41.33
- **OpenTelemetry**: 2.25.0-alpha
- **Kotest**: 6.1.3
- **Kover**: 0.9.4
- **eunoia**: 0.1.0 (internal shared libraries)

## Testing Conventions

- **Framework**: Kotest (not JUnit assertions)
- **Coverage**: Kover (auto-finalizes after every test run)
- **Exclusions**: 
  - Application classes (`*Application*`)
  - QueryDSL Q-classes (`Q*`)
  - Generated code (`com.lomeone.generated.*`)
- **Databases**: MySQL (production), H2 (test)

## Code Generation

- **QueryDSL**: Q-classes generated via KSP during compilation
- **DGS GraphQL**: Code generation from GraphQL schemas (if present)
- **JPA**: allOpen plugin auto-applied to `@Entity`, `@MappedSuperclass`, `@Embeddable`

## Build Configuration

- **Version**: Git commit hash (10 chars) from `git rev-parse --short=10 HEAD`
- **Image Registry**: `396428372646.dkr.ecr.ap-northeast-2.amazonaws.com`
- **Jib**: Container image builds for `api` module
- **Coverage Reports**: 
  - Coveralls: `build/reports/kover/report.xml`
  - SonarCloud: organization `lomeone`, project key `lomeone_lomeone-server`

## Observability

- Spring Boot Actuator endpoints
- OpenTelemetry instrumentation (Spring Boot + AWS SDK)
- Prometheus metrics
- Micrometer tracing
