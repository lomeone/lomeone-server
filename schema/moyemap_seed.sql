-- ============================================================================
-- Moyemap Venues Seed Data
-- 수집 출처: hyostime.com, gatheringhouse.site, yeonplace.imweb.me,
--            litt.ly/solomon_party, litt.ly/mannam_lounge_e,
--            litt.ly/blackparty_official, litt.ly/withmate
-- ============================================================================

INSERT INTO venues (
    title, category, status,
    location_name, location_address, location_latitude, location_longitude,
    location_region, location_city, location_district,
    price, currency, image_url, description, source_url, tags,
    created_at, updated_at
) VALUES

-- 1. 효스타임 신촌점
(
    '효스타임 신촌점',
    'SOCIAL_PARTY', 'PUBLISHED',
    '효스타임 신촌점', '서울특별시 서대문구 신촌로 (예약 후 상세 안내)',
    37.5551, 126.9368,
    '신촌/홍대', '서울특별시', '서대문구',
    0, 'KRW',
    'https://www.hyostime.com/images/logo/logo-preview.png',
    '전국 최대규모 소셜링 파티. 250명이 만들어내는 활기찬 에너지 속에서 자유롭게 연결되는 특별한 소셜링을 경험하세요.',
    'https://www.hyostime.com/',
    '["소셜파티","250명","대규모","신촌","20대","30대","MZ세대"]',
    NOW(), NOW()
),

-- 2. 효스타임 강남점
(
    '효스타임 강남점',
    'SOCIAL_PARTY', 'PUBLISHED',
    '효스타임 강남점', '서울특별시 강남구 (예약 후 상세 안내)',
    37.4981, 127.0276,
    '강남', '서울특별시', '강남구',
    0, 'KRW',
    'https://www.hyostime.com/images/logo/logo-preview.png',
    '무드 있는 분위기, 고급진 시설로 완성되는 정제된 공간. 도심 속 프라이빗함 속에서 품격 있는 만남을 경험하세요.',
    'https://www.hyostime.com/',
    '["소셜파티","프리미엄","강남","고급시설","20대","30대"]',
    NOW(), NOW()
),

-- 3. 효스타임 망원점 (로테이션 소개팅)
(
    '효스타임 망원점',
    'ROTATION_DATING', 'PUBLISHED',
    '효스타임 망원점', '서울특별시 마포구 망원동 (예약 후 상세 안내)',
    37.5556, 126.9073,
    '망원/합정', '서울특별시', '마포구',
    0, 'KRW',
    'https://www.hyostime.com/images/logo/logo-preview.png',
    '시설도 분위기도 전국 최고급. 초역세권의 편한 접근성과 미술관을 연상케 하는 최고급 시설에서 진행하는 로테이션 소개팅.',
    'https://www.hyostime.com/',
    '["로테이션소개팅","최고급시설","망원","초역세권","20대","30대"]',
    NOW(), NOW()
),

-- 4. 효스타임 신림점
(
    '효스타임 신림점',
    'SOCIAL_PARTY', 'PUBLISHED',
    '효스타임 신림점', '서울특별시 관악구 신림동 (예약 후 상세 안내)',
    37.4843, 126.9297,
    '신림', '서울특별시', '관악구',
    0, 'KRW',
    'https://www.hyostime.com/images/logo/logo-preview.png',
    '칵테일과 위스키가 달콤한 묘약으로 변하는 마법 같은 공간. 신림점만의 압도적인 분위기를 경험하세요.',
    'https://www.hyostime.com/',
    '["소셜파티","칵테일","위스키","신림","20대","30대"]',
    NOW(), NOW()
),

-- 5. 게더링하우스
(
    '게더링하우스',
    'SOCIAL_PARTY', 'PUBLISHED',
    '게더링하우스', '서울특별시 서대문구 명물길 19 가동 지하1·2층',
    37.5574, 126.9351,
    '신촌/홍대', '서울특별시', '서대문구',
    0, 'KRW',
    'https://cdn.imweb.me/upload/S20250901bfc7cfb1e20de/a3c052defbe0e.png',
    '20~30대 MZ세대를 위한 소셜 파티 브랜드. 누구나 자연스럽게 어울리며 새로운 인연을 만들 수 있는 라운지 감성의 공간.',
    'https://www.gatheringhouse.site/',
    '["소셜파티","MZ세대","라운지","신촌","20대","30대","게더링"]',
    NOW(), NOW()
),

-- 6. 연플레이스
(
    '연플레이스',
    'SOLO_PARTY', 'PUBLISHED',
    '연플레이스 합정점', '서울특별시 마포구 합정동 (네이버 지도 예약 후 안내)',
    37.5497, 126.9143,
    '합정', '서울특별시', '마포구',
    0, 'KRW',
    'https://cdn.imweb.me/upload/S20250703bb9f3aa1153f5/046e904034e52.png',
    '처음 만난 사람이 인연으로 남는 곳. 대화 중심의 소셜링 파티로 평일 30명, 주말 70명 규모로 진행됩니다. 평일 20:00, 주말 19:00 시작.',
    'https://yeonplace.imweb.me/',
    '["솔로파티","대화중심","합정","20대","30대","소셜링","인연"]',
    NOW(), NOW()
),

-- 7. 솔로몬파티
(
    '솔로몬파티',
    'SOCIAL_PARTY', 'PUBLISHED',
    '솔로몬파티 서초점', '서울특별시 서초구 (신청 완료 후 공지 안내)',
    37.4837, 127.0324,
    '서초', '서울특별시', '서초구',
    0, 'KRW',
    'https://cdn.litt.ly/images/CeEfFMDIOuUnZb0Tuy4le6npNsBQS9HM?s=1200x630&m=inside',
    '현직 방송 출연진도 내돈내산으로 방문하는 프리미엄 소셜 클럽. 목/일 반반파티, 금/토 연애프로그램 파티로 운영. 올해만 3커플 결혼 확정.',
    'https://litt.ly/solomon_party',
    '["소셜파티","프리미엄","연애프로그램","반반파티","서초","20대","30대"]',
    NOW(), NOW()
),

-- 8. 만남회관
(
    '만남회관',
    'SOLO_PARTY', 'PUBLISHED',
    '만남회관 문래점', '서울특별시 영등포구 문래동 (문래역 도보 5분)',
    37.5173, 126.8960,
    '문래', '서울특별시', '영등포구',
    0, 'KRW',
    'https://cdn.litt.ly/images/sVQRCCGp31twnjQXJe9RyyOzlJddbjhe?s=1200x630&m=inside',
    '120명 수용 전용 공간에서 함께하는 설렘 넘치는 솔로파티. 메인디쉬 3~4가지, 핑거푸드, 술·음료 무제한 제공. 토/일 1차(20:10~23:00), 2차(23:10~01:30).',
    'https://litt.ly/mannam_lounge_e',
    '["솔로파티","120명","문래","음식포함","주류무제한","토일","20대","30대"]',
    NOW(), NOW()
),

-- 9. 블랙파티 (상세 주소 미확인 - DRAFT)
(
    '블랙파티',
    'SOCIAL_PARTY', 'DRAFT',
    '블랙파티', '서울특별시 (상세 위치 확인 필요)',
    37.5665, 126.9780,
    '서울', '서울특별시', NULL,
    0, 'KRW',
    'https://cdn.litt.ly/images/uVFZopHbslMmkSvqpmmvL7gG01jPf4yv?s=600x600&m=outside&f=webp',
    '블랙파티 공식 소셜파티. 상세 정보는 인스타그램 @blackparty__official 참고.',
    'https://www.instagram.com/blackparty__official/',
    '["소셜파티","블랙파티","20대","30대"]',
    NOW(), NOW()
),

-- 10. withmate (상세 정보 미확인 - DRAFT)
(
    'withmate',
    'SOCIAL_PARTY', 'DRAFT',
    'withmate', '서울특별시 (상세 위치 확인 필요)',
    37.5665, 126.9780,
    '서울', '서울특별시', NULL,
    0, 'KRW',
    'https://cdn.litt.ly/images/aYgdVsxHrhTqYpG58IZkC2WzIADb8LWo?s=500x220&m=outside&f=webp',
    'withmate 소셜파티. 상세 정보는 litt.ly/withmate 참고.',
    'https://litt.ly/withmate',
    '["소셜파티","withmate","20대","30대"]',
    NOW(), NOW()
);
