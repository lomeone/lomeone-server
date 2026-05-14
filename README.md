# LOMEONE Server

[![Coverage Status](https://coveralls.io/repos/github/comstering/lomeone-server/badge.svg?branch=develop)](https://coveralls.io/github/comstering/lomeone-server?branch=develop)
[![codecov](https://codecov.io/gh/lomeone/lomeone-server/graph/badge.svg?token=8YCWcT8JMq)](https://codecov.io/gh/lomeone/lomeone-server)
[![Preview Deploy](https://github.com/comstering/lomeone-server/actions/workflows/production.yaml/badge.svg)](https://github.com/comstering/lomeone-server/actions/workflows/preview.yaml)
[![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/comstering/mms-server/blob/develop/LICENSE)

## 제품 요구사항 문서

### 1. 제품 개요

`lomeone-server`는 Lomeone의 여러 서비스를 하나의 서버에서 운영하기 위한
통합 백엔드 프로젝트입니다. 현재는 다음 두 제품의 백엔드 역할을 합니다.

- `texhol`: 포커 매장, 게임 세션, 예약, 플레이어, 바이인/리바이 관리
- `moyemap`: 장소 탐색, 지도 기반 검색, 리뷰, 평점, 외부 링크 관리

이 서버는 앞으로 새로운 Lomeone 제품이 추가되더라도 각 제품의 도메인 규칙이
섞이지 않도록, 제품별 패키지와 비즈니스 경계를 분리하는 것을 목표로 합니다.

### 2. 목표

- `texhol` 운영에 필요한 예약, 게임, 플레이어, 결제 흐름을 안정적으로 제공한다.
- `moyemap`의 장소 탐색과 리뷰 기능을 확장할 수 있는 기반을 제공한다.
- 제품별 도메인 로직을 명확히 분리한다.
- API, 배포, 관측성, 인증/권한 같은 공통 백엔드 요구사항을 한 프로젝트 안에서 관리한다.
- 신규 제품 도메인을 추가할 때 기존 구조를 크게 변경하지 않도록 한다.

### 3. 비목표

- 이 저장소는 프론트엔드 애플리케이션을 포함하지 않는다.
- 제품별 화면 구성이나 UI 워크플로우는 이 문서의 범위가 아니다.
- 수동으로 관리하는 DDL에서 외래키 제약은 운영 정책에 따라 의도적으로 생략할 수 있다.
- 완전한 멀티테넌시는 현재 단계의 필수 요구사항이 아니다.

### 4. 주요 사용자

- `texhol` 매장 관리자: 매장, 게임 타입, 게임 세션, 예약, 참가자 정보를 관리한다.
- `texhol` 운영자: 참가자 상태, 바이인/리바이, 결제 수단 통계를 관리한다.
- `moyemap` 사용자: 지역, 카테고리, 가격, 평점을 기준으로 장소를 탐색한다.
- 내부 개발자: 여러 제품을 하나의 백엔드 코드베이스에서 안정적으로 확장한다.

### 5. 현재 제품 범위

#### Texhol

현재 구현된 주요 도메인은 다음과 같습니다.

- 매장 생성 및 조회
- 플레이어 생성, 조회, find-or-create 흐름
- 매장별 게임 타입 생성
- 게임 세션 생성, 조회, 목록 조회, 상태 변경
- 예약 생성, 등록, 취소, 플레이어 변경, 조회
- 게임 엔트리 생성, 리바이 추가, 싯아웃 토글, 플레이어 변경, 결제 통계

중요한 비즈니스 규칙은 다음과 같습니다.

- 매장 이름은 중복될 수 없다.
- 플레이어 닉네임은 중복될 수 없다.
- 게임 타입 이름은 같은 매장 안에서 중복될 수 없다.
- 게임 세션은 매장, 게임 타입, 세션 번호 조합으로 중복될 수 없다.
- 예약은 게임 세션과 플레이어 조합으로 중복될 수 없다.
- 게임 엔트리는 게임 세션과 플레이어 조합으로 중복될 수 없다.
- 예약은 대기 상태에서만 등록 상태로 변경할 수 있다.
- 취소된 예약은 다시 등록할 수 없다.
- 싯아웃 상태의 게임 엔트리는 리바이를 추가할 수 없다.

#### Moyemap

현재 구현된 주요 도메인은 다음과 같습니다.

- 장소 정보: 카테고리, 위치, 가격, 이미지, 설명, 원본 URL, 평균 평점
- 장소에 연결된 리뷰
- 카테고리, 지역, 가격 범위, 최소 평점, 평점순 정렬 기반 장소 필터링
- 리뷰 생성 및 장소별 리뷰 조회

### 6. API 요구사항

- 현재 API 계층은 Netflix DGS 기반 GraphQL을 중심으로 구성한다.
- GraphQL schema는 `api/src/main/resources/schema` 아래에 둔다.
- 생성된 GraphQL 타입은 `com.lomeone.generated` 패키지를 사용한다.
- 제품 워크플로우에 REST API가 더 적합한 경우 이후 REST 엔드포인트를 추가할 수 있다.
- API 응답은 도메인 상태를 표현하되, 영속성 구현 세부사항을 외부에 노출하지 않는다.

### 7. 기능 요구사항

#### Texhol 기능 요구사항

- 관리자는 매장을 등록할 수 있다.
- 관리자는 매장별 게임 타입을 정의할 수 있다.
- 관리자는 매장과 게임 타입을 기준으로 게임 세션을 생성할 수 있다.
- 운영자는 플레이어의 예약을 생성할 수 있다.
- 운영자는 예약을 실제 게임 참가 엔트리로 등록할 수 있다.
- 운영자는 예약을 취소할 수 있다.
- 운영자는 예약 없이 직접 게임 엔트리를 생성할 수 있다.
- 운영자는 활성 상태의 게임 엔트리에만 리바이를 추가할 수 있다.
- 운영자는 게임 엔트리를 활성 상태와 싯아웃 상태 사이에서 전환할 수 있다.
- 운영자는 게임 세션의 결제 수단별 집계를 조회할 수 있다.

#### Moyemap 기능 요구사항

- 사용자는 조건을 선택해 장소 목록을 조회할 수 있다.
- 사용자는 장소 상세 정보를 조회할 수 있다.
- 사용자는 장소에 리뷰를 작성할 수 있다.
- 리뷰 데이터는 장소의 평균 평점에 반영된다.
- 장소의 원본 URL은 외부 이동과 추후 클릭 추적을 위해 보존한다.

### 8. 데이터 요구사항

주요 데이터 그룹은 다음과 같습니다.

- `stores`
- `players`
- `game_types`
- `game_sessions`
- `reservations`
- `game_entries`
- `buy_in_records`
- `venues`
- `reviews`

`texhol` 운영 데이터는 현재 MySQL을 기준으로 합니다. `moyemap`은 지도 기반
검색이 고도화되면 PostgreSQL/PostGIS 같은 지리 데이터 저장소 도입을 검토합니다.

### 9. 품질 요구사항

- 도메인 로직은 Kotest 기반 테스트로 검증한다.
- 데이터베이스 유니크 제약이나 JPA 동작에 의존하는 비즈니스 규칙은 통합 테스트를 추가한다.
- 비즈니스 검증 실패는 원시 데이터베이스 예외가 아니라 도메인 예외로 표현한다.
- QueryDSL Q-class 같은 생성 코드는 직접 수정하지 않는다.
- Spring Boot Actuator, OpenTelemetry, Micrometer, Prometheus 호환 메트릭을 통해 관측성을 확보한다.

### 10. 아키텍처

이 프로젝트는 Kotlin/Spring Boot 기반의 멀티 모듈 Gradle 프로젝트입니다.

```text
api
├── domain
└── infrastructure
    └── domain

domain
└── util

util
```

- `api`: 애플리케이션 진입점, GraphQL DataFetcher, API 설정
- `domain`: 엔티티, 비즈니스 서비스, repository interface, 도메인 예외
- `infrastructure`: 데이터소스, AWS, OpenFeign, 외부 연동 설정
- `util`: 공통 유틸리티 코드

### 11. 기술 스택

- Kotlin 2.3.10
- Java 21
- Spring Boot 4.0.3
- Spring Data JPA
- QueryDSL with KSP
- Netflix DGS GraphQL
- SpringDoc OpenAPI
- OpenFeign
- AWS SDK
- OpenTelemetry
- Kotest
- Kover
- Jib

### 12. 빌드 및 테스트

```bash
./gradlew build
./gradlew test
./gradlew :domain:test
./gradlew :api:test
./gradlew :infrastructure:test
./gradlew koverHtmlReport
./gradlew koverXmlReport
```

필수 환경:

- Java 21
- `GITHUB_ACTOR`
- `GITHUB_TOKEN`

`GITHUB_ACTOR`와 `GITHUB_TOKEN`은 GitHub Packages에 있는 내부 `eunoia`
의존성을 받기 위해 필요합니다.

### 13. 배포

- 배포 대상 애플리케이션은 `api` 모듈이다.
- 컨테이너 이미지는 Jib으로 빌드한다.
- 이미지 레지스트리: `396428372646.dkr.ecr.ap-northeast-2.amazonaws.com`
- 버전은 현재 Git commit hash를 기준으로 생성한다.

### 14. 로드맵

#### Phase 1: Texhol 백엔드

- texhol GraphQL API 완성
- 예약과 게임 엔트리 워크플로우 안정화
- 관리자/운영자 권한을 위한 인증 및 인가 추가
- 핵심 도메인 제약에 대한 persistence-level 통합 테스트 추가

#### Phase 2: Moyemap 백엔드

- 장소 검색과 지도 기능 확장
- 외부 링크 클릭 추적 추가
- 지리 검색을 위한 PostgreSQL/PostGIS 도입 검토
- 자주 조회되는 장소 데이터 캐싱

#### Phase 3: 공통 플랫폼

- 공통 인증 및 권한 체계 추가
- 배포 파이프라인 개선
- 환경별 설정 관리 강화
- 관측성과 알림 체계 강화

#### Phase 4: 신규 제품 도메인

- 신규 제품 추가를 위한 반복 가능한 패키지/모듈 컨벤션 정리
- 제품 요구사항이 충분해질 때 멀티테넌시 도입 검토
