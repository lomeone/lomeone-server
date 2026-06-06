package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.repository.VenueRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class CreateVenueTest : BehaviorSpec({
    val venueRepository = mockk<VenueRepository>()
    val createVenue = CreateVenue(venueRepository)

    Given("새로운 Venue 정보가 주어졌을 때") {
        every { venueRepository.save(any()) } answers { firstArg() }

        When("Venue를 생성하면") {
            val command = CreateVenueCommand(
                title = "홍대 금요일 밤 소셜파티",
                category = VenueCategory.SOCIAL_PARTY,
                locationName = "홍대입구역 3번 출구 라운지",
                address = "서울특별시 마포구 양화로 160",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남",
                minPrice = 25000,
                imageUrl = "https://example.com/party.jpg",
                description = "매주 금요일 소셜 파티",
                sourceUrl = "https://example.com/party/1"
            )

            val result = createVenue(command)

            Then("Venue가 생성되고 결과가 반환된다") {
                result.title shouldBe "홍대 금요일 밤 소셜파티"
                result.category shouldBe VenueCategory.SOCIAL_PARTY
                result.location.name shouldBe "홍대입구역 3번 출구 라운지"
                result.location.address shouldBe "서울특별시 마포구 양화로 160"
                result.minPrice shouldBe 25000
                result.currency shouldBe "KRW"

                verify {
                    venueRepository.save(match { it.title == "홍대 금요일 밤 소셜파티" })
                }
            }
        }
    }

    Given("태그를 포함한 Venue 정보가 주어졌을 때") {
        every { venueRepository.save(any()) } answers { firstArg() }

        When("tags를 함께 제공하면") {
            val command = CreateVenueCommand(
                title = "홍대 소셜파티",
                category = VenueCategory.SOCIAL_PARTY,
                locationName = "홍대입구역",
                address = "서울특별시 마포구",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남",
                minPrice = 25000,
                imageUrl = "main.jpg",
                description = "파티",
                sourceUrl = "source",
                tags = listOf("소셜파티", "금요밤")
            )

            val result = createVenue(command)

            Then("태그가 저장된다") {
                result.tags shouldBe listOf("소셜파티", "금요밤")
            }
        }

        When("tags를 제공하지 않으면") {
            val command = CreateVenueCommand(
                title = "홍대 소셜파티",
                category = VenueCategory.SOCIAL_PARTY,
                locationName = "홍대입구역",
                address = "서울특별시 마포구",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남",
                imageUrl = "main.jpg",
                description = "파티",
                sourceUrl = "source"
            )

            val result = createVenue(command)

            Then("빈 목록으로 초기화된다") {
                result.tags shouldBe emptyList()
            }
        }
    }
})
