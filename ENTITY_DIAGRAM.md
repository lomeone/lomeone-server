# Entity Diagram

## Texhol — 홀덤 매장 운영

```mermaid
erDiagram
    Store {
        Long    store_id    PK
        String  name        "UK NOT NULL"
        String  location    "NOT NULL"
        String  address     "nullable"
        String  imageUrl    "NOT NULL"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Player {
        Long    player_id   PK
        String  nickname    "UK NOT NULL"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Game {
        Long    game_id     PK
        Long    store_id    FK
        String  gameType    "NOT NULL"
        Int     session     "NOT NULL"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    GameType {
        Long    game_type_id    PK
        Long    store_id        FK
        String  name            "NOT NULL"
        String  scheduleType    "DAILY|WEEKLY|MONTHLY|CUSTOM"
        Int     dayOfWeek       "nullable  0-6  WEEKLY 전용"
        String  description     "nullable"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    GameSession {
        Long    game_session_id PK
        Long    store_id        FK
        Long    game_type_id    FK
        Int     session         "NOT NULL"
        String  status          "RECRUITING|EARLY_CLOSE|CLOSED"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    GameEntry {
        Long    game_entry_id   PK
        Long    game_session_id FK
        Long    player_id       FK
        String  status          "ALIVE|SIT_OUT"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    BuyInRecord {
        Long    game_entry_id   FK
        String  type            "INITIAL|RE_BUY"
        String  paymentMethod   "CASH|CARD|POINTS"
        LocalDateTime createdAt
    }

    Reservation {
        Long    reservation_id  PK
        Long    game_session_id FK
        Long    player_id       FK
        String  time            "ex. 1900  5분뒤  현장"
        String  status          "WAITING|REGISTERED|CANCELLED"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Store        ||--o{ Game        : "보유"
    Store        ||--o{ GameType    : "정의"
    Store        ||--o{ GameSession : "개최"
    GameType     ||--o{ GameSession : "스케줄"
    GameSession  ||--o{ GameEntry   : "참여자"
    GameSession  ||--o{ Reservation : "예약"
    Player       ||--o{ GameEntry   : "참가"
    Player       ||--o{ Reservation : "예약"
    GameEntry    ||--o{ BuyInRecord : "바이인 기록"
```

---

## Moyemap — 소셜링 이벤트 지도

```mermaid
erDiagram
    SocialingEvent {
        Long    socialing_event_id  PK
        String  title               "NOT NULL"
        String  purpose             "SOCIAL_PARTY|SOLO_PARTY|GUESTHOUSE_PARTY|ROTATION_DATING|NETWORKING|HONSOOL_BAR|BAR|WORKSHOP|ETC"
        Text    description         "NOT NULL"
        Int     price               ">=0"
        String  currency            "default KRW"
        String  location_name       "Embedded NOT NULL"
        String  location_address    "Embedded NOT NULL"
        Double  location_latitude   "Embedded -90~90"
        Double  location_longitude  "Embedded -180~180"
        String  location_region     "Embedded NOT NULL"
        String  location_city       "Embedded nullable"
        String  location_district   "Embedded nullable"
        String  sourceUrl           "http/https"
        String  imageUrl            "http/https"
        Text    tags                "JSON Array"
        String  status              "DRAFT|PUBLISHED|HIDDEN|ENDED"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Venue {
        Long    venue_id            PK
        String  title               "NOT NULL"
        String  category            "SOCIAL|BAR|GUESTHOUSE|NETWORKING"
        String  location_name       "Embedded NOT NULL"
        String  location_address    "Embedded NOT NULL"
        Double  location_latitude   "Embedded"
        Double  location_longitude  "Embedded"
        String  location_region     "Embedded NOT NULL"
        String  location_city       "Embedded nullable"
        String  location_district   "Embedded nullable"
        Int     price               "NOT NULL"
        String  imageUrl            "NOT NULL"
        Text    description         "NOT NULL"
        String  sourceUrl           "NOT NULL"
        Text    images              "JSON Array"
        Text    amenities           "JSON Array"
        Text    tags                "JSON Array"
        Double  averageRating       "default 0.0"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Review {
        Long    review_id   PK
        Long    venue_id    FK
        String  userName    "NOT NULL"
        Int     rating      "1~5"
        Text    comment     "NOT NULL"
        LocalDateTime createdAt
        LocalDateTime updatedAt
    }

    Venue ||--o{ Review : "리뷰"
```

---

## 상태 머신 (State Machines)

```mermaid
stateDiagram-v2
    direction LR

    state "GameSession" as GS {
        [*] --> RECRUITING
        RECRUITING --> EARLY_CLOSE : earlyClose()
        RECRUITING --> CLOSED      : close()
        EARLY_CLOSE --> RECRUITING : reOpen()
        EARLY_CLOSE --> CLOSED     : close()
    }
```

```mermaid
stateDiagram-v2
    direction LR

    state "Reservation" as R {
        [*] --> WAITING
        WAITING    --> REGISTERED : register()
        WAITING    --> CANCELLED  : cancel()
        REGISTERED --> CANCELLED  : cancel()
    }
```

```mermaid
stateDiagram-v2
    direction LR

    state "GameEntry" as GE {
        [*] --> ALIVE
        ALIVE   --> SIT_OUT : sitOut()
        SIT_OUT --> ALIVE   : returnToGame()
    }
```

```mermaid
stateDiagram-v2
    direction LR

    state "SocialingEvent" as SE {
        [*] --> DRAFT
        DRAFT     --> PUBLISHED : publish()
        PUBLISHED --> HIDDEN    : hide()
        HIDDEN    --> PUBLISHED : publish()
        PUBLISHED --> ENDED     : end()
        HIDDEN    --> ENDED     : end()
    }
```

---

## 전체 구조 요약

```mermaid
graph TD
    subgraph Texhol["🎰 Texhol — 홀덤 매장 운영"]
        Store["Store\n매장"]
        Player["Player\n플레이어"]
        Game["Game\n게임 메타"]
        GameType["GameType\n게임 유형"]
        GameSession["GameSession\n게임 세션"]
        GameEntry["GameEntry\n참여 엔트리"]
        BuyInRecord["BuyInRecord\n바이인 기록\n@Embeddable"]
        Reservation["Reservation\n사전 예약"]

        Store --> Game
        Store --> GameType
        Store --> GameSession
        GameType --> GameSession
        GameSession --> GameEntry
        GameSession --> Reservation
        Player --> GameEntry
        Player --> Reservation
        GameEntry --> BuyInRecord
    end

    subgraph Moyemap["🗺️ Moyemap — 소셜링 이벤트 지도"]
        SocialingEvent["SocialingEvent\n소셜링 이벤트"]
        Venue["Venue\n장소"]
        Review["Review\n리뷰"]
        Location["Location\n위치\n@Embeddable"]

        SocialingEvent -. "Embedded" .-> Location
        Venue -. "Embedded" .-> Location
        Venue --> Review
    end

    subgraph Common["🔧 공통 인프라"]
        AuditEntity["AuditEntity\ncreatedAt / updatedAt\n@MappedSuperclass"]
    end

    Texhol -. "extends" .-> AuditEntity
    Moyemap -. "extends" .-> AuditEntity
```

---

## 유니크 제약 (Unique Constraints)

| 테이블 | 유니크 키 | 설명 |
|---|---|---|
| `stores` | `name` | 매장명 중복 불가 |
| `players` | `nickname` | 닉네임 중복 불가 |
| `games` | `(store_id, game_type, session)` | 매장·유형·회차 조합 |
| `game_types` | `(store_id, name)` | 매장 내 게임 유형명 |
| `game_sessions` | `(store_id, game_type_id, session)` | 매장·유형·회차 조합 |
| `game_entries` | `(game_session_id, player_id)` | 세션 내 플레이어 중복 참가 불가 |
| `reservations` | `(game_session_id, player_id)` | 세션 내 플레이어 중복 예약 불가 |

## 인덱스 (Indexes)

| 테이블 | 인덱스 컬럼 | 목적 |
|---|---|---|
| `game_sessions` | `(store_id, status)` | 매장별 진행 중 세션 조회 |
| `game_sessions` | `game_type_id` | 게임 유형별 세션 조회 |
| `game_entries` | `player_id` | 플레이어별 참가 이력 조회 |
| `reservations` | `player_id` | 플레이어별 예약 조회 |
| `moyemap_socialing_events` | `status` | 게시된 이벤트 필터 |
| `moyemap_socialing_events` | `purpose` | 목적별 필터 |
| `moyemap_socialing_events` | `location_region` | 지역 필터 |
| `moyemap_socialing_events` | `price` | 가격 필터 |
| `moyemap_socialing_events` | `(location_latitude, location_longitude)` | 지도 범위 조회 |
| `venues` | `category` | 카테고리 필터 |
| `venues` | `location_region` | 지역 필터 |
| `venues` | `average_rating` | 평점 정렬 |
| `reviews` | `venue_id` | 장소별 리뷰 조회 |
| `reviews` | `rating` | 평점 필터 |
