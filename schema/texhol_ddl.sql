-- ============================================================================
-- Texhol Database DDL (MySQL)
-- ============================================================================
-- Generated from lomeone-server domain entities
--
-- Execution Order:
-- 1. stores
-- 2. players
-- 3. game_types
-- 4. game_sessions
-- 5. reservations
-- 6. game_entries
-- 7. buy_in_records
-- 8. games (legacy - deprecated in favor of game_sessions)
--
-- ============================================================================

-- Drop tables in reverse order (to handle foreign key constraints)
DROP TABLE IF EXISTS buy_in_records;
DROP TABLE IF EXISTS game_entries;
DROP TABLE IF EXISTS reservations;
DROP TABLE IF EXISTS game_sessions;
DROP TABLE IF EXISTS game_types;
DROP TABLE IF EXISTS games;
DROP TABLE IF EXISTS players;
DROP TABLE IF EXISTS stores;

-- ============================================================================
-- Table: stores
-- Description: 매장 정보
-- ============================================================================
CREATE TABLE stores (
    store_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '매장 ID (PK)',
    name VARCHAR(255) NOT NULL COMMENT '매장명',
    location VARCHAR(32) NOT NULL COMMENT '위치 (시/구)',
    address VARCHAR(128) NULL COMMENT '상세 주소',
    image_url VARCHAR(255) NOT NULL COMMENT '이미지 URL',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (store_id),
    UNIQUE KEY idx_stores_name_u1 (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='매장';

-- ============================================================================
-- Table: players
-- Description: 플레이어 정보
-- ============================================================================
CREATE TABLE players (
    player_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '플레이어 ID (PK)',
    nickname VARCHAR(255) NOT NULL COMMENT '닉네임',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (player_id),
    UNIQUE KEY idx_players_nickname_u1 (nickname)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='플레이어';

-- ============================================================================
-- Table: game_types
-- Description: 게임 타입 (데일리, 주간, 월간 등)
-- ============================================================================
CREATE TABLE game_types (
    game_type_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게임 타입 ID (PK)',
    store_id BIGINT NOT NULL COMMENT '매장 ID (FK)',
    name VARCHAR(100) NOT NULL COMMENT '게임 타입명 (예: 데일리, 몬스터, 메인)',
    schedule_type VARCHAR(20) NOT NULL COMMENT '스케줄 타입 (DAILY, WEEKLY, MONTHLY, CUSTOM)',
    day_of_week INT NULL COMMENT '요일 (0-6, Sunday-Saturday), WEEKLY일 때만 사용',
    description VARCHAR(500) NULL COMMENT '설명',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (game_type_id),
    UNIQUE KEY idx_game_types_store_id_name_u1 (store_id, name),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게임 타입';

-- ============================================================================
-- Table: game_sessions
-- Description: 게임 세션 (실제 진행되는 게임)
-- ============================================================================
CREATE TABLE game_sessions (
    game_session_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게임 세션 ID (PK)',
    store_id BIGINT NOT NULL COMMENT '매장 ID (FK)',
    game_type_id BIGINT NOT NULL COMMENT '게임 타입 ID (FK)',
    session INT NOT NULL COMMENT '세션 번호 (1차, 2차 등)',
    status VARCHAR(20) NOT NULL DEFAULT 'RECRUITING' COMMENT '상태 (RECRUITING, EARLY_CLOSE, CLOSED)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (game_session_id),
    UNIQUE KEY idx_game_sessions_store_id_game_type_id_session_u1 (store_id, game_type_id, session),
    KEY idx_game_sessions_store_id_status (store_id, status),
    KEY idx_game_sessions_game_type_id (game_type_id),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게임 세션';

-- ============================================================================
-- Table: reservations
-- Description: 예약 정보
-- ============================================================================
CREATE TABLE reservations (
    reservation_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '예약 ID (PK)',
    game_session_id BIGINT NOT NULL COMMENT '게임 세션 ID (FK)',
    player_id BIGINT NOT NULL COMMENT '플레이어 ID (FK)',
    time VARCHAR(50) NOT NULL COMMENT '예약 시간 (예: "20:30", "현장")',
    status VARCHAR(20) NOT NULL DEFAULT 'WAITING' COMMENT '상태 (WAITING, REGISTERED, CANCELLED)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (reservation_id),
    UNIQUE KEY idx_reservations_game_session_id_player_id_u1 (game_session_id, player_id),
    KEY idx_reservations_player_id (player_id),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='예약';

-- ============================================================================
-- Table: game_entries
-- Description: 게임 참가 엔트리 (실제 게임에 참여한 플레이어)
-- ============================================================================
CREATE TABLE game_entries (
    game_entry_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게임 엔트리 ID (PK)',
    game_session_id BIGINT NOT NULL COMMENT '게임 세션 ID (FK)',
    player_id BIGINT NOT NULL COMMENT '플레이어 ID (FK)',
    status VARCHAR(20) NOT NULL DEFAULT 'ALIVE' COMMENT '상태 (ALIVE, SIT_OUT)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (game_entry_id),
    UNIQUE KEY idx_game_entries_game_session_id_player_id_u1 (game_session_id, player_id),
    KEY idx_game_entries_player_id (player_id),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게임 엔트리';

-- ============================================================================
-- Table: buy_in_records
-- Description: 바이인 기록 (초기 바이인 + 리바이)
-- Note: ElementCollection으로 game_entries에 종속
-- ============================================================================
CREATE TABLE buy_in_records (
    game_entry_id BIGINT NOT NULL COMMENT '게임 엔트리 ID (FK)',
    type VARCHAR(20) NOT NULL COMMENT '타입 (INITIAL, RE_BUY)',
    payment_method VARCHAR(20) NOT NULL COMMENT '결제 수단 (CASH, CARD, POINTS)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',

    KEY idx_buy_in_records_game_entry_id (game_entry_id),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='바이인 기록';

-- ============================================================================
-- Table: games (DEPRECATED - Legacy)
-- Description: 구 게임 테이블 (game_sessions으로 대체됨)
-- Note: 하위 호환성을 위해 유지, 신규 개발 시 game_sessions 사용 권장
-- ============================================================================
CREATE TABLE games (
    game_id BIGINT NOT NULL AUTO_INCREMENT COMMENT '게임 ID (PK)',
    store_id BIGINT NOT NULL COMMENT '매장 ID (FK)',
    game_type VARCHAR(255) NOT NULL COMMENT '게임 타입 (문자열)',
    session INT NOT NULL COMMENT '세션 번호',
    status VARCHAR(20) NOT NULL DEFAULT 'RECRUITING' COMMENT '상태 (RECRUITING, EARLY_CLOSE, CLOSED)',
    created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '생성일시',
    updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (game_id),
    UNIQUE KEY idx_games_store_id_game_type_session_u1 (store_id, game_type, session),
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='게임 (Legacy - game_sessions 사용 권장)';

-- ============================================================================
-- Sample Data (Optional)
-- ============================================================================

-- Insert sample store
INSERT INTO stores (name, location, address, image_url, created_at, updated_at) VALUES
('파이널나인 샤로수길', '서울시 관악구', '서울시 관악구 샤로수길 123', 'https://picsum.photos/seed/final9-1/600/400', NOW(), NOW()),
('KMGM 이태원', '서울시 용산구', '서울시 용산구 이태원로 456', 'https://picsum.photos/seed/kmgm-1/600/400', NOW(), NOW());

-- Insert sample players
INSERT INTO players (nickname, created_at, updated_at) VALUES
('김철수', NOW(), NOW()),
('이영희', NOW(), NOW()),
('박지민', NOW(), NOW());

-- Insert sample game types
INSERT INTO game_types (store_id, name, schedule_type, day_of_week, description, created_at, updated_at) VALUES
(1, '데일리', 'DAILY', NULL, '매일 진행', NOW(), NOW()),
(1, '몬스터', 'DAILY', NULL, '매일 진행', NOW(), NOW()),
(1, '메인', 'WEEKLY', 6, '매주 토요일', NOW(), NOW());

-- Insert sample game sessions
INSERT INTO game_sessions (store_id, game_type_id, session, status, created_at, updated_at) VALUES
(1, 1, 1, 'RECRUITING', NOW(), NOW()),
(1, 2, 1, 'RECRUITING', NOW(), NOW());

-- ============================================================================
-- Verification Queries
-- ============================================================================

-- Show all tables
-- SHOW TABLES;

-- Show table structures
-- DESCRIBE stores;
-- DESCRIBE players;
-- DESCRIBE game_types;
-- DESCRIBE game_sessions;
-- DESCRIBE reservations;
-- DESCRIBE game_entries;
-- DESCRIBE buy_in_records;
-- DESCRIBE games;

-- Show indexes
-- SHOW INDEX FROM stores;
-- SHOW INDEX FROM game_sessions;
-- SHOW INDEX FROM reservations;
-- SHOW INDEX FROM game_entries;

-- Show foreign keys
-- SELECT
--     TABLE_NAME,
--     COLUMN_NAME,
--     CONSTRAINT_NAME,
--     REFERENCED_TABLE_NAME,
--     REFERENCED_COLUMN_NAME
-- FROM INFORMATION_SCHEMA.KEY_COLUMN_USAGE
-- WHERE TABLE_SCHEMA = DATABASE()
--     AND REFERENCED_TABLE_NAME IS NOT NULL
-- ORDER BY TABLE_NAME, CONSTRAINT_NAME;
