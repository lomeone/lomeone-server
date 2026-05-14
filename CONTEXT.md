# CONTEXT.md

**프로젝트**: lomeone-server  
**목적**: moyemap, texhol 및 향후 신규 프로젝트의 통합 백엔드 서버  
**마지막 업데이트**: 2026-04-22

## 현재 상태

### 완료된 기능
- ✅ Multi-module Gradle 프로젝트 구조 (api, domain, infrastructure, util)
- ✅ Spring Boot 4.0.3 + Kotlin 2.3.10 기반
- ✅ JPA + QueryDSL (KSP) 설정
- ✅ Netflix DGS (GraphQL) 설정
- ✅ OpenTelemetry 관측성 설정
- ✅ Kover 코드 커버리지
- ✅ eunoia 라이브러리 의존성 (GitHub Packages)
- ✅ Domain 구조 (reservation, game, store, player, common)

### 진행 중인 작업
- 🚧 texhol 도메인 API 개발
- 🚧 REST/GraphQL 엔드포인트 구현

## 최근 변경 사항

*아직 기록 없음*

## TODO / 계획

### Phase 1: texhol 백엔드 (우선순위 높음)
- [ ] Domain Layer
  - [x] Entity 설계 (reservation, game, store, player)
  - [ ] Repository interface 정의
  - [ ] Service (Use Case) 구현
    - [ ] 게임 생성/마감
    - [ ] 예약 관리
    - [ ] 플레이어 바이인/리바이
    - [ ] 정산 로직
- [ ] Infrastructure Layer
  - [ ] Repository 구현 (JPA + QueryDSL)
  - [ ] AWS DynamoDB 연동 (필요시)
  - [ ] External API 클라이언트 (OpenFeign)
- [ ] API Layer
  - [ ] REST API 엔드포인트
    - [ ] 매장 관리 API
    - [ ] 게임 관리 API
    - [ ] 예약 API
    - [ ] 플레이어 API
  - [ ] GraphQL Schema 및 Resolver
  - [ ] API 문서화 (SpringDoc OpenAPI)
- [ ] 인증/권한
  - [ ] JWT 기반 인증
  - [ ] Role 기반 권한 (GUEST/MANAGER/ADMIN)
  - [ ] Spring Security 설정

### Phase 2: moyemap 백엔드
- [ ] Domain Layer
  - [ ] Event (모임) entity
  - [ ] Venue (장소) entity
  - [ ] Category entity
  - [ ] External Link tracking
- [ ] Infrastructure Layer
  - [ ] PostgreSQL + PostGIS 연동 (지리 데이터)
  - [ ] Redis 캐싱
  - [ ] 외부 데이터 소스 연동
- [ ] API Layer
  - [ ] 지도 기반 검색 API
  - [ ] 필터링 API
  - [ ] 상세 조회 API
  - [ ] 아웃링크 클릭 추적

### Phase 3: 공통 인프라
- [ ] 실시간 기능
  - [ ] WebSocket 설정 (Spring WebFlux or STOMP)
  - [ ] Server-Sent Events
- [ ] 배포 파이프라인
  - [ ] Docker 이미지 빌드 (Jib)
  - [ ] GitHub Actions CI/CD
  - [ ] AWS ECS/EKS 배포
  - [ ] 환경별 설정 관리 (dev/staging/prod)
- [ ] 관측성
  - [ ] 로그 집계 (CloudWatch or ELK)
  - [ ] 메트릭 수집 (Prometheus)
  - [ ] 분산 추적 (OpenTelemetry)
  - [ ] 에러 추적 (Sentry or Datadog)
- [ ] 성능 최적화
  - [ ] 쿼리 최적화
  - [ ] N+1 문제 해결
  - [ ] 캐싱 전략
  - [ ] Connection pool 튜닝

### Phase 4: 신규 프로젝트 확장
- [ ] 모듈화된 도메인 추가 용이성 확보
- [ ] Multi-tenancy 고려 (필요시)

## 알려진 이슈

*아직 기록 없음*

## 기술 부채

- AWS SDK Kotlin DynamoDB Mapper 플러그인 미사용 (preview 대기 중)
- H2 test runtime only 설정 주석 처리됨
- 코드 커버리지 최소 기준 0% (minBound 설정 필요)

## Module Dependencies

```
api (entry point)
├─> domain (business logic)
│   └─> util
└─> infrastructure (external integrations)
    └─> domain
```

## Database

- **Production**: MySQL 9.6.0
- **Test**: H2 2.4.240
- **Future**: PostgreSQL + PostGIS (moyemap 지리 데이터)

## 배포 정보

- **Image Registry**: `396428372646.dkr.ecr.ap-northeast-2.amazonaws.com`
- **Jib**: Container image builds for `api` module
- **Version**: Git commit hash (10 chars)

## Coverage & Quality

- **Coveralls**: https://coveralls.io/github/comstering/lomeone-server
- **Codecov**: https://codecov.io/gh/lomeone/lomeone-server
- **SonarCloud**: organization `lomeone`, project key `lomeone_lomeone-server`

## 환경 변수

### Required
- `GITHUB_ACTOR` - GitHub Packages 인증
- `GITHUB_TOKEN` - GitHub Packages 인증

### Application (추가 예정)
- Database credentials
- AWS credentials
- JWT secret
- External API keys

## 참고 사항

- eunoia 라이브러리 버전: 0.1.0
- QueryDSL Q-classes는 빌드 시 자동 생성 (KSP)
- allOpen plugin으로 JPA entity lazy loading 지원
- OpenTelemetry 자동 계측 활성화
