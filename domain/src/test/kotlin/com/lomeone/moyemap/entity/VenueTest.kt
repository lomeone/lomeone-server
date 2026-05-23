package com.lomeone.moyemap.entity

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class VenueTest : FreeSpec({
    fun createLocation() = Location(
        name = "홍대입구역 3번 출구 라운지",
        address = "서울특별시 마포구 양화로 160",
        latitude = 37.5571,
        longitude = 126.9243,
        region = "홍대/연남"
    )

    "Venue 생성" - {
        "필수 정보로 Venue를 생성할 수 있다" {
            val venue = Venue(
                title = "홍대 금요일 밤 소셜파티",
                category = VenueCategory.SOCIAL_PARTY,
                location = createLocation(),
                price = 25000,
                imageUrl = "https://example.com/party.jpg",
                description = "매주 금요일 소셜 파티",
                sourceUrl = "https://example.com/party/1"
            )

            venue.title shouldBe "홍대 금요일 밤 소셜파티"
            venue.category shouldBe VenueCategory.SOCIAL_PARTY
            venue.price shouldBe 25000
            venue.currency shouldBe "KRW"
            venue.status shouldBe VenueStatus.DRAFT
            venue.tags shouldBe emptyList()
        }

        "생성 시 기본 상태는 DRAFT다" {
            val venue = Venue("venue", VenueCategory.BAR, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")
            venue.status shouldBe VenueStatus.DRAFT
        }

        "통화 기본값은 KRW다" {
            val venue = Venue("venue", VenueCategory.BAR, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")
            venue.currency shouldBe "KRW"
        }

        "태그를 포함해서 생성할 수 있다" {
            val venue = Venue(
                title = "venue",
                category = VenueCategory.SOCIAL_PARTY,
                location = createLocation(),
                price = 10000,
                imageUrl = "url",
                description = "desc",
                sourceUrl = "source",
                tags = listOf("20대", "30대", "와인")
            )
            venue.tags shouldBe listOf("20대", "30대", "와인")
        }

        "빈 제목으로 생성하면 예외가 발생한다" {
            shouldThrow<IllegalArgumentException> {
                Venue("", VenueCategory.BAR, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")
            }
        }

        "음수 가격으로 생성하면 예외가 발생한다" {
            shouldThrow<IllegalArgumentException> {
                Venue("venue", VenueCategory.BAR, createLocation(), -1, imageUrl = "url", description = "desc", sourceUrl = "source")
            }
        }

        "가격 0은 허용된다" {
            val venue = Venue("venue", VenueCategory.HONSOOL_BAR, createLocation(), 0, imageUrl = "url", description = "desc", sourceUrl = "source")
            venue.price shouldBe 0
        }
    }

    "Venue 상태 변경" - {
        "DRAFT 상태에서 게시할 수 있다" {
            val venue = Venue("venue", VenueCategory.BAR, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")

            venue.publish()

            venue.status shouldBe VenueStatus.PUBLISHED
        }

        "HIDDEN 상태에서도 게시할 수 있다" {
            val venue = Venue("venue", VenueCategory.BAR, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")
            venue.publish()
            venue.hide()

            venue.publish()

            venue.status shouldBe VenueStatus.PUBLISHED
        }

        "이미 PUBLISHED 상태에서 게시하면 예외가 발생한다" {
            val venue = Venue("venue", VenueCategory.BAR, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")
            venue.publish()

            shouldThrow<IllegalStateException> {
                venue.publish()
            }
        }

        "PUBLISHED 상태에서 숨길 수 있다" {
            val venue = Venue("venue", VenueCategory.BAR, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")
            venue.publish()

            venue.hide()

            venue.status shouldBe VenueStatus.HIDDEN
        }

        "PUBLISHED 상태가 아닌 경우 숨기면 예외가 발생한다" {
            val venue = Venue("venue", VenueCategory.BAR, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")

            shouldThrow<IllegalStateException> {
                venue.hide()
            }
        }
    }

    "Venue 정보 수정" - {
        val venue = Venue("원래 제목", VenueCategory.SOCIAL_PARTY, createLocation(), 20000, imageUrl = "old.jpg", description = "원래 설명", sourceUrl = "old-url")

        "제목을 수정할 수 있다" {
            venue.update(title = "새 제목")
            venue.title shouldBe "새 제목"
        }

        "카테고리를 수정할 수 있다" {
            venue.update(category = VenueCategory.BAR)
            venue.category shouldBe VenueCategory.BAR
        }

        "가격을 수정할 수 있다" {
            venue.update(price = 30000)
            venue.price shouldBe 30000
        }

        "태그를 수정할 수 있다" {
            venue.update(tags = listOf("루프탑", "와인"))
            venue.tags shouldBe listOf("루프탑", "와인")
        }
    }

    "위치 정보 수정" - {
        "새로운 Location으로 교체할 수 있다" {
            val venue = Venue("venue", VenueCategory.SOCIAL_PARTY, createLocation(), 10000, imageUrl = "url", description = "desc", sourceUrl = "source")

            venue.updateLocation(Location(
                name = "강남역 11번 출구",
                address = "서울특별시 강남구 테헤란로 123",
                latitude = 37.4979,
                longitude = 127.0276,
                region = "강남"
            ))

            venue.location.name shouldBe "강남역 11번 출구"
            venue.location.region shouldBe "강남"
        }
    }
})
