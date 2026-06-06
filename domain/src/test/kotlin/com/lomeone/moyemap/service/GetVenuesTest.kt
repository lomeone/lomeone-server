package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.entity.VenueStatus
import com.lomeone.moyemap.repository.VenueRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetVenuesTest : BehaviorSpec({
    val venueRepository = mockk<VenueRepository>()
    val getVenues = GetVenues(venueRepository)

    val hongdaeLocation = Location("홍대", "서울 마포구", 37.5571, 126.9243, "홍대/연남")
    val gangnamLocation = Location("강남", "서울 강남구", 37.4979, 127.0276, "강남")

    val venue1 = Venue("소셜파티1", VenueCategory.SOCIAL_PARTY, hongdaeLocation, minPrice = 25000, imageUrl = "img1", description = "desc1", sourceUrl = "url1")
    val venue2 = Venue("혼술바1", VenueCategory.HONSOOL_BAR, gangnamLocation, minPrice = 30000, imageUrl = "img2", description = "desc2", sourceUrl = "url2")
    val venue3 = Venue("소셜파티2", VenueCategory.SOCIAL_PARTY, gangnamLocation, minPrice = 20000, imageUrl = "img3", description = "desc3", sourceUrl = "url3")

    Given("필터 없이 조회할 때") {
        every { venueRepository.findByStatus(VenueStatus.PUBLISHED) } returns listOf(venue1, venue2, venue3)

        When("모든 Venue를 조회하면") {
            val result = getVenues(GetVenuesQuery())

            Then("PUBLISHED 상태의 Venue 목록이 반환된다") {
                result shouldHaveSize 3
            }
        }
    }

    Given("카테고리 필터가 주어졌을 때") {
        every { venueRepository.findByStatus(VenueStatus.PUBLISHED) } returns listOf(venue1, venue2, venue3)

        When("특정 카테고리로 조회하면") {
            val result = getVenues(GetVenuesQuery(categories = listOf(VenueCategory.SOCIAL_PARTY)))

            Then("해당 카테고리의 Venue만 반환된다") {
                result shouldHaveSize 2
                result.all { it.category == VenueCategory.SOCIAL_PARTY } shouldBe true
            }
        }
    }

    Given("지역 필터가 주어졌을 때") {
        every { venueRepository.findByStatus(VenueStatus.PUBLISHED) } returns listOf(venue1, venue2, venue3)

        When("특정 지역으로 조회하면") {
            val result = getVenues(GetVenuesQuery(region = "홍대/연남"))

            Then("해당 지역의 Venue만 반환된다") {
                result shouldHaveSize 1
                result[0].location.region shouldBe "홍대/연남"
            }
        }
    }

    Given("카테고리와 지역 필터가 함께 주어졌을 때") {
        every { venueRepository.findByStatus(VenueStatus.PUBLISHED) } returns listOf(venue1, venue2, venue3)

        When("카테고리와 지역으로 조회하면") {
            val result = getVenues(GetVenuesQuery(categories = listOf(VenueCategory.SOCIAL_PARTY), region = "강남"))

            Then("두 조건을 모두 만족하는 Venue만 반환된다") {
                result shouldHaveSize 1
                result[0].category shouldBe VenueCategory.SOCIAL_PARTY
                result[0].location.region shouldBe "강남"
            }
        }
    }

    Given("가격 범위 필터가 주어졌을 때") {
        every { venueRepository.findByStatus(VenueStatus.PUBLISHED) } returns listOf(venue1, venue2, venue3)

        When("가격 범위로 조회하면") {
            val result = getVenues(GetVenuesQuery(minPrice = 20000, maxPrice = 25000))

            Then("가격 범위 내의 Venue만 반환된다") {
                result shouldHaveSize 2
                result.all { it.minPrice != null && it.minPrice in 20000..25000 } shouldBe true
            }
        }
    }

    Given("minPrice가 null인 Venue가 포함되어 있을 때") {
        val venueNoPrice = Venue("가격미확인", VenueCategory.BAR, hongdaeLocation, imageUrl = "img", description = "desc", sourceUrl = "url")
        every { venueRepository.findByStatus(VenueStatus.PUBLISHED) } returns listOf(venue1, venueNoPrice)

        When("가격 범위 필터를 적용하면") {
            val result = getVenues(GetVenuesQuery(maxPrice = 30000))

            Then("minPrice가 null인 Venue는 결과에서 제외된다") {
                result shouldHaveSize 1
                result[0].title shouldBe "소셜파티1"
            }
        }
    }
})
