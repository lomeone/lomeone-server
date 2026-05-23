-- ============================================================================
-- Moyemap Database DDL (MySQL)
-- ============================================================================
-- Generated from lomeone-server domain entities
--
-- Execution Order:
-- 1. venues
-- 2. reviews
--
-- ============================================================================

-- Drop tables in reverse order (to handle foreign key constraints)
DROP TABLE IF EXISTS reviews;
DROP TABLE IF EXISTS venues;

-- ============================================================================
-- Table: venues
-- Description: 소셜링 장소 정보 (파티, 바, 로테이션 소개팅 등)
-- ============================================================================
CREATE TABLE venues (
    venue_id     BIGINT NOT NULL AUTO_INCREMENT  COMMENT '장소 ID (PK)',
    title        VARCHAR(255) NOT NULL            COMMENT '장소 제목',
    category     VARCHAR(30) NOT NULL             COMMENT '카테고리 (SOCIAL_PARTY, SOLO_PARTY, GUESTHOUSE_PARTY, ROTATION_DATING, NETWORKING, HONSOOL_BAR, BAR, WORKSHOP, ETC)',
    status       VARCHAR(20) NOT NULL DEFAULT 'DRAFT' COMMENT '상태 (DRAFT, PUBLISHED, HIDDEN)',

    -- 위치 정보 (embedded Location)
    location_name       VARCHAR(255) NOT NULL     COMMENT '장소명 (예: 홍대입구역 3번 출구 라운지)',
    location_address    VARCHAR(500) NOT NULL     COMMENT '도로명 주소',
    location_latitude   DOUBLE NOT NULL           COMMENT '위도',
    location_longitude  DOUBLE NOT NULL           COMMENT '경도',
    location_region     VARCHAR(100) NOT NULL     COMMENT '지역 (예: 홍대/연남, 강남)',
    location_city       VARCHAR(100) NULL         COMMENT '시/도',
    location_district   VARCHAR(100) NULL         COMMENT '구/군',

    price        INT NOT NULL                     COMMENT '가격 (원)',
    currency     VARCHAR(10) NOT NULL DEFAULT 'KRW' COMMENT '통화',
    image_url    VARCHAR(500) NOT NULL            COMMENT '대표 이미지 URL',
    description  TEXT NOT NULL                    COMMENT '설명',
    source_url   VARCHAR(500) NOT NULL            COMMENT '원본 페이지 URL',
    tags         TEXT NOT NULL                    COMMENT '태그 목록 (JSON 배열, 예: ["20대","와인"])',

    created_at   DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)                    COMMENT '생성일시',
    updated_at   DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (venue_id),
    KEY idx_venues_category     (category),
    KEY idx_venues_status       (status),
    KEY idx_venues_region       (location_region)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='소셜링 장소';

-- ============================================================================
-- Table: reviews
-- Description: 장소 리뷰 (Phase 2)
-- ============================================================================
CREATE TABLE reviews (
    review_id   BIGINT NOT NULL AUTO_INCREMENT   COMMENT '리뷰 ID (PK)',
    venue_id    BIGINT NOT NULL                  COMMENT '장소 ID (FK)',
    user_name   VARCHAR(255) NOT NULL            COMMENT '작성자 닉네임',
    rating      INT NOT NULL                     COMMENT '평점 (1-5)',
    comment     TEXT NOT NULL                    COMMENT '리뷰 내용',

    created_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6)                    COMMENT '생성일시',
    updated_at  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '수정일시',

    PRIMARY KEY (review_id),
    KEY idx_reviews_venue_id    (venue_id),
    KEY idx_reviews_rating      (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='장소 리뷰';

-- ============================================================================
-- Sample Data (Optional)
-- ============================================================================

-- Insert sample venues
INSERT INTO venues (title, category, status, location_name, location_address, location_latitude, location_longitude, location_region, location_city, location_district, price, currency, image_url, description, source_url, tags, created_at, updated_at) VALUES
('홍대 금요일 밤 소셜파티', 'SOCIAL_PARTY', 'PUBLISHED',
 '홍대입구역 3번 출구 라운지', '서울특별시 마포구 양화로 160', 37.5571, 126.9243, '홍대/연남', '서울특별시', '마포구',
 25000, 'KRW', 'https://picsum.photos/seed/venue-1/600/400', '매주 금요일 진행되는 소셜 파티',
 'https://example.com/party/1', '["20대","30대","와인","소셜"]', NOW(), NOW()),

('강남 혼술바 라운지', 'HONSOOL_BAR', 'PUBLISHED',
 '강남역 11번 출구', '서울특별시 강남구 테헤란로 123', 37.4979, 127.0276, '강남', '서울특별시', '강남구',
 15000, 'KRW', 'https://picsum.photos/seed/venue-2/600/400', '혼자 즐기는 프리미엄 혼술바',
 'https://example.com/bar/1', '["혼술","와인","위스키"]', NOW(), NOW()),

('이태원 로테이션 소개팅', 'ROTATION_DATING', 'DRAFT',
 '이태원역 2번 출구 루프탑', '서울특별시 용산구 이태원로 456', 37.5340, 126.9946, '이태원/한남', '서울특별시', '용산구',
 35000, 'KRW', 'https://picsum.photos/seed/venue-3/600/400', '이태원 루프탑에서 진행하는 로테이션 소개팅',
 'https://example.com/dating/1', '["소개팅","20대","30대","루프탑"]', NOW(), NOW());

-- ============================================================================
-- Verification Queries
-- ============================================================================

-- Show all tables
-- SHOW TABLES;

-- Show table structures
-- DESCRIBE venues;
-- DESCRIBE reviews;

-- Show indexes
-- SHOW INDEX FROM venues;
-- SHOW INDEX FROM reviews;

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
