package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.entity.VenueStatus
import com.lomeone.moyemap.repository.VenueRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class SubmitVenueTest : BehaviorSpec({
    val venueRepository = mockk<VenueRepository>()
    val submitVenue = SubmitVenue(venueRepository)

    Given("일반 사용자가 Venue 정보를 제출할 때") {
        every { venueRepository.save(any()) } answers { firstArg() }

        When("필수 정보만 제공하면") {
            val command = SubmitVenueCommand(
                title = "홍대 소셜파티",
                category = VenueCategory.SOCIAL_PARTY,
                sourceUrl = "https://example.com/party",
                locationName = "홍대입구역 라운지",
                locationAddress = "서울특별시 마포구 양화로 160",
                locationRegion = "홍대/연남",
                description = "매주 금요일 소셜 파티"
            )

            val result = submitVenue(command)

            Then("DRAFT 상태로 저장된다") {
                result.status shouldBe VenueStatus.DRAFT
            }

            Then("위도/경도가 0.0으로 설정된다") {
                result.location.latitude shouldBe 0.0
                result.location.longitude shouldBe 0.0
            }

            Then("기본 통화는 KRW다") {
                result.currency shouldBe "KRW"
            }

            Then("repository save가 호출된다") {
                verify { venueRepository.save(match { it.title == "홍대 소셜파티" }) }
            }
        }

        When("minPrice를 함께 제공하면") {
            val command = SubmitVenueCommand(
                title = "강남 로테이션 소개팅",
                category = VenueCategory.ROTATION_DATING,
                sourceUrl = "https://example.com/dating",
                locationName = "강남역 근처",
                locationAddress = "서울특별시 강남구",
                locationRegion = "강남",
                description = "로테이션 소개팅",
                minPrice = 35000
            )

            val result = submitVenue(command)

            Then("minPrice가 저장된다") {
                result.minPrice shouldBe 35000
            }
        }

        When("minPrice를 제공하지 않으면") {
            val command = SubmitVenueCommand(
                title = "신촌 네트워킹",
                category = VenueCategory.NETWORKING,
                sourceUrl = "https://example.com/networking",
                locationName = "신촌",
                locationAddress = "서울특별시 서대문구",
                locationRegion = "신촌/홍대",
                description = "네트워킹 이벤트"
            )

            val result = submitVenue(command)

            Then("minPrice는 null이다") {
                result.minPrice shouldBe null
            }
        }

        When("imageUrl을 제공하면") {
            val command = SubmitVenueCommand(
                title = "홍대 소셜파티",
                category = VenueCategory.SOCIAL_PARTY,
                sourceUrl = "https://example.com/party",
                locationName = "홍대입구역",
                locationAddress = "서울특별시 마포구",
                locationRegion = "홍대/연남",
                description = "파티",
                imageUrl = "https://example.com/image.jpg"
            )

            val result = submitVenue(command)

            Then("imageUrl이 저장된다") {
                result.imageUrl shouldBe "https://example.com/image.jpg"
            }
        }

        When("imageUrl을 제공하지 않으면") {
            val command = SubmitVenueCommand(
                title = "홍대 소셜파티",
                category = VenueCategory.SOCIAL_PARTY,
                sourceUrl = "https://example.com/party",
                locationName = "홍대입구역",
                locationAddress = "서울특별시 마포구",
                locationRegion = "홍대/연남",
                description = "파티"
            )

            val result = submitVenue(command)

            Then("imageUrl은 빈 문자열이다") {
                result.imageUrl shouldBe ""
            }
        }

        When("tags를 함께 제공하면") {
            val command = SubmitVenueCommand(
                title = "문래 솔로파티",
                category = VenueCategory.SOLO_PARTY,
                sourceUrl = "https://example.com/solo",
                locationName = "문래역",
                locationAddress = "서울특별시 영등포구",
                locationRegion = "문래",
                description = "솔로파티",
                tags = listOf("솔로파티", "30대", "음식포함")
            )

            val result = submitVenue(command)

            Then("tags가 저장된다") {
                result.tags shouldBe listOf("솔로파티", "30대", "음식포함")
            }
        }

        When("tags를 제공하지 않으면") {
            val command = SubmitVenueCommand(
                title = "문래 솔로파티",
                category = VenueCategory.SOLO_PARTY,
                sourceUrl = "https://example.com/solo",
                locationName = "문래역",
                locationAddress = "서울특별시 영등포구",
                locationRegion = "문래",
                description = "솔로파티"
            )

            val result = submitVenue(command)

            Then("빈 목록으로 초기화된다") {
                result.tags shouldBe emptyList()
            }
        }
    }
})
