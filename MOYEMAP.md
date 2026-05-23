# MOYEMAP MVP PRD

## 1. 제품 목적

Moyemap MVP의 목적은 전국 소셜링 정보를 지도 위에 빠르게 노출하고, 사용자가 마음에
드는 소셜링을 발견하면 원본 페이지로 이동하게 하는 것이다.

초기 서버는 예약, 결제, 리뷰, 개인화보다 다음 6가지를 가장 빠르게 제공하는 데 집중한다.

- 지도에 위치 표시
- 지도 마커에 가격 표시
- 각 소셜링 원본 페이지 링크 제공
- 각 소셜링 목적/카테고리 표시
- 태그 표시
- 대표 이미지 표시

모든 API는 Netflix DGS GraphQL로 제공한다.

## 2. MVP 범위

### 포함

- 소셜링 이벤트 등록
- 소셜링 이벤트 수정
- 소셜링 이벤트 숨김/게시 상태 변경
- 소셜링 이벤트 상세 조회
- 소셜링 이벤트 목록 조회
- 지도 영역 기반 마커 조회
- 카테고리, 지역, 태그, 가격 범위 필터
- 원본 페이지 URL 제공
- 대표 이미지 URL 제공

### 제외

- 리뷰
- Event Tracking
- Moyemap 내부 신청
- Moyemap 내부 결제
- 사용자 계정 기반 찜/저장
- 크롤링/자동 수집
- 관리자 UI
- 추천/랭킹

## 3. MVP 이후 범위

### Phase 2: 리뷰

- 각 소셜링 리뷰 작성
- 평점 집계
- 리뷰 검수/숨김

### Phase 3: Event Tracking

- 상세 조회 이벤트 기록
- 원본 링크 클릭 기록
- 필터/검색 조건 snapshot 기록
- 카테고리/지역/가격대별 전환 분석

### Phase 4: 중개 서비스

- Moyemap 내부 네트워킹 신청
- 신청자 정보 수집
- 좌석/정원 관리
- 결제 생성과 결제 완료 처리
- 신청 취소/환불 정책
- 호스트 정산

## 4. 빠른 MVP 도메인 엔티티

MVP에서는 출시 속도를 위해 `SocialingEvent` 하나를 중심 aggregate로 둔다.
`Venue`, `Host`, `Review`, `Payment`는 지금 만들지 않는다. 장소명, 주소, 좌표,
원본 링크, 이미지, 태그는 모두 이벤트에 직접 포함한다.

### 4.1 SocialingEvent

지도에 표시되는 소셜링의 핵심 엔티티다.

필드:

- `id`: Long
- `title`: String
- `purpose`: SocialingPurpose
- `description`: String
- `price`: Int
- `currency`: String
- `location`: SocialingLocation
- `sourceUrl`: String
- `imageUrl`: String
- `tags`: List<String>
- `status`: SocialingEventStatus
- `createdAt`: Instant
- `updatedAt`: Instant

선택 필드:

- `startedAt`: LocalDateTime?
- `endedAt`: LocalDateTime?
- `hostName`: String?
- `sourceName`: String?

MVP 필수값:

- 제목
- 목적/카테고리
- 가격
- 장소명
- 주소
- 위도
- 경도
- 지역
- 원본 URL
- 대표 이미지 URL
- 게시 상태

역할:

- 목록 카드 데이터 제공
- 지도 마커 데이터 제공
- 상세 페이지 데이터 제공
- 원본 페이지 이동 버튼 데이터 제공

### 4.2 SocialingLocation

이벤트 위치를 표현하는 embeddable value object다.

필드:

- `placeName`: String
- `address`: String
- `region`: String
- `latitude`: Double
- `longitude`: Double

선택 필드:

- `city`: String?
- `district`: String?

검증:

- `placeName`은 비어 있을 수 없다.
- `address`는 비어 있을 수 없다.
- `region`은 비어 있을 수 없다.
- `latitude`는 -90 이상 90 이하만 허용한다.
- `longitude`는 -180 이상 180 이하만 허용한다.

### 4.3 SocialingPurpose

소셜링의 목적/카테고리다. MVP에서는 enum으로 관리한다.

값:

- `SOCIAL_PARTY`: 소셜파티
- `SOLO_PARTY`: 솔로파티
- `GUESTHOUSE_PARTY`: 게스트하우스파티
- `ROTATION_DATING`: 로테이션 소개팅
- `NETWORKING`: 네트워킹
- `HONSOOL_BAR`: 혼술 바
- `BAR`: 바/펍
- `WORKSHOP`: 클래스/워크숍
- `ETC`: 기타

### 4.4 SocialingEventStatus

사용자 노출 상태다.

값:

- `DRAFT`: 작성 중
- `PUBLISHED`: 사용자 노출
- `HIDDEN`: 숨김
- `ENDED`: 종료

MVP 조회 API는 기본적으로 `PUBLISHED`만 반환한다.

### 4.5 Tags

태그는 별도 엔티티로 만들지 않고 문자열 배열로 저장한다.

예시:

- `20대`
- `30대`
- `와인`
- `루프탑`
- `퇴근후`
- `초보환영`
- `혼자참여`
- `서울`
- `강남`

초기 저장 방식:

- JPA entity 내부에서는 JSON 문자열 또는 element collection 중 빠른 방식을 선택한다.
- GraphQL 응답에서는 `[String!]!`로 제공한다.

추후 태그 검색 품질이 중요해지면 `moyemap_socialing_event_tags` 테이블로 분리한다.

## 5. 지금 만들지 않을 엔티티

빠른 MVP에서는 아래 엔티티를 만들지 않는다.

- `Venue`: 같은 장소 재사용, 장소 리뷰, 장소 상세가 필요해질 때 분리한다.
- `Host`: 호스트 프로필과 호스트별 모임 조회가 필요해질 때 분리한다.
- `Review`: MVP 이후 Phase 2에서 만든다.
- `ClickLog`: MVP 이후 Event Tracking에서 만든다.
- `Reservation/Application`: Moyemap 내부 신청 기능에서 만든다.
- `Payment`: 결제 중개 서비스 단계에서 만든다.

## 6. DGS GraphQL 요구사항

### Query

```graphql
extend type Query {
  socialingEvents(input: SocialingEventSearchInput): [SocialingEvent!]!
  socialingEvent(id: ID!): SocialingEvent!
  socialingEventMarkers(input: SocialingEventMarkerSearchInput): [SocialingEventMarker!]!
}
```

### Mutation

```graphql
extend type Mutation {
  createSocialingEvent(input: CreateSocialingEventInput!): SocialingEventPayload!
  updateSocialingEvent(input: UpdateSocialingEventInput!): SocialingEventPayload!
  publishSocialingEvent(id: ID!): SocialingEventPayload!
  hideSocialingEvent(id: ID!): SocialingEventPayload!
}
```

### Type

```graphql
type SocialingEvent {
  id: ID!
  title: String!
  purpose: SocialingPurpose!
  description: String!
  price: Int!
  currency: String!
  location: SocialingLocation!
  sourceUrl: String!
  imageUrl: String!
  tags: [String!]!
  status: SocialingEventStatus!
  startedAt: String
  endedAt: String
  hostName: String
  sourceName: String
  createdAt: String!
  updatedAt: String!
}

type SocialingEventMarker {
  id: ID!
  title: String!
  purpose: SocialingPurpose!
  price: Int!
  currency: String!
  latitude: Float!
  longitude: Float!
  region: String!
  imageUrl: String!
  tags: [String!]!
}

type SocialingLocation {
  placeName: String!
  address: String!
  region: String!
  city: String
  district: String
  latitude: Float!
  longitude: Float!
}
```

### Enum

```graphql
enum SocialingPurpose {
  SOCIAL_PARTY
  SOLO_PARTY
  GUESTHOUSE_PARTY
  ROTATION_DATING
  NETWORKING
  HONSOOL_BAR
  BAR
  WORKSHOP
  ETC
}

enum SocialingEventStatus {
  DRAFT
  PUBLISHED
  HIDDEN
  ENDED
}
```

### Input

```graphql
input SocialingEventSearchInput {
  purposes: [SocialingPurpose!]
  region: String
  tags: [String!]
  minPrice: Int
  maxPrice: Int
  keyword: String
  includeHidden: Boolean
}

input SocialingEventMarkerSearchInput {
  northEastLatitude: Float
  northEastLongitude: Float
  southWestLatitude: Float
  southWestLongitude: Float
  purposes: [SocialingPurpose!]
  region: String
  tags: [String!]
  minPrice: Int
  maxPrice: Int
}

input CreateSocialingEventInput {
  title: String!
  purpose: SocialingPurpose!
  description: String!
  price: Int!
  currency: String
  placeName: String!
  address: String!
  region: String!
  city: String
  district: String
  latitude: Float!
  longitude: Float!
  sourceUrl: String!
  imageUrl: String!
  tags: [String!]
  startedAt: String
  endedAt: String
  hostName: String
  sourceName: String
}

input UpdateSocialingEventInput {
  id: ID!
  title: String
  purpose: SocialingPurpose
  description: String
  price: Int
  currency: String
  placeName: String
  address: String
  region: String
  city: String
  district: String
  latitude: Float
  longitude: Float
  sourceUrl: String
  imageUrl: String
  tags: [String!]
  startedAt: String
  endedAt: String
  hostName: String
  sourceName: String
}

type SocialingEventPayload {
  event: SocialingEvent!
}
```

## 7. Repository 요구사항

`SocialingEventRepository`가 제공해야 할 기능:

- ID로 조회
- 생성/수정 저장
- 게시된 이벤트 목록 조회
- 필터 조건 기반 목록 조회
- 지도 영역 내 게시 이벤트 조회
- 상태 변경 저장

MVP 검색 조건:

- `status = PUBLISHED`
- `purpose in (...)`
- `location.region = ...`
- `price between minPrice and maxPrice`
- `latitude/longitude` 지도 경계 포함
- `tags` 포함 검색
- `title`, `description`, `placeName`, `address` 키워드 검색

## 8. 데이터베이스 초안

MVP 테이블은 하나로 시작한다.

### moyemap_socialing_events

컬럼:

- `socialing_event_id`
- `title`
- `purpose`
- `description`
- `price`
- `currency`
- `place_name`
- `address`
- `region`
- `city`
- `district`
- `latitude`
- `longitude`
- `source_url`
- `image_url`
- `tags`
- `status`
- `started_at`
- `ended_at`
- `host_name`
- `source_name`
- `created_at`
- `updated_at`

인덱스:

- `idx_moyemap_socialing_events_status`
- `idx_moyemap_socialing_events_purpose`
- `idx_moyemap_socialing_events_region`
- `idx_moyemap_socialing_events_price`
- `idx_moyemap_socialing_events_latitude_longitude`
- `idx_moyemap_socialing_events_started_at`

## 9. 검증 규칙

- 제목은 비어 있을 수 없다.
- 설명은 비어 있을 수 없다.
- 가격은 0 이상이어야 한다.
- 통화 기본값은 `KRW`다.
- 장소명은 비어 있을 수 없다.
- 주소는 비어 있을 수 없다.
- 지역은 비어 있을 수 없다.
- 위도는 -90 이상 90 이하만 허용한다.
- 경도는 -180 이상 180 이하만 허용한다.
- 원본 URL은 HTTP/HTTPS URL이어야 한다.
- 대표 이미지 URL은 HTTP/HTTPS URL이어야 한다.
- 태그는 공백을 제거하고 빈 값은 저장하지 않는다.
- `startedAt`과 `endedAt`이 모두 있으면 `startedAt <= endedAt`이어야 한다.

## 10. 응답 사용처

### 지도 마커

필요 필드:

- ID
- 제목
- 목적
- 가격
- 통화
- 위도
- 경도
- 지역
- 대표 이미지
- 태그

### 리스트 카드

필요 필드:

- ID
- 제목
- 목적
- 가격
- 지역
- 장소명
- 대표 이미지
- 태그
- 시작 일시

### 상세

필요 필드:

- 목록 카드 필드 전체
- 설명
- 주소
- 원본 URL
- 호스트명
- 출처명
- 생성/수정 시각

## 11. 구현 순서

1. `SocialingPurpose`, `SocialingEventStatus` enum 추가
2. `SocialingLocation` embeddable 추가
3. `SocialingEvent` entity 추가
4. `SocialingEventRepository` interface 추가
5. 조회/생성/수정/게시/숨김 use case service 추가
6. DGS GraphQL schema 추가
7. DGS DataFetcher 추가
8. repository 구현과 QueryDSL 필터 조회 추가
9. domain test 추가
10. API smoke test 추가

## 12. 기존 초안과의 차이

기존 `Venue`, `Review` 중심 구조는 장소 탐색 서비스에는 적합하지만, 지금 MVP의 핵심은
장소가 아니라 지도 위에 소셜링 이벤트를 빠르게 보여주는 것이다.

따라서 MVP에서는 다음처럼 단순화한다.

- `Venue` 분리 없음
- `Review` 구현 없음
- 장소 정보는 `SocialingEvent.location`에 직접 포함
- 원본 링크는 별도 `SourceLink` 없이 `sourceUrl` 하나로 시작
- 대표 이미지는 `imageUrl` 하나로 시작
- 태그는 문자열 배열로 시작

이 구조는 빠르게 출시하기 좋고, 이후 데이터가 쌓이면 `Venue`, `Host`, `Review`,
`ClickLog`, `Application`, `Payment`로 자연스럽게 분리할 수 있다.
