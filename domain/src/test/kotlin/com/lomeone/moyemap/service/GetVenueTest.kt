package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.VenueRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class GetVenueTest : BehaviorSpec({
    val venueRepository = mockk<VenueRepository>()
    val getVenue = GetVenue(venueRepository)

    Given("존재하는 Venue ID가 주어졌을 때") {
        val location = Location("홍대", "서울 마포구", 37.5571, 126.9243, "홍대/연남")
        val venue = Venue("소셜파티", VenueCategory.SOCIAL_PARTY, location, 25000, imageUrl = "img", description = "desc", sourceUrl = "url")

        every { venueRepository.findById(1L) } returns java.util.Optional.of(venue)

        When("해당 ID로 조회하면") {
            val result = getVenue(1L)

            Then("Venue가 반환된다") {
                result shouldBe venue
            }
        }
    }

    Given("존재하지 않는 Venue ID가 주어졌을 때") {
        every { venueRepository.findById(999L) } returns java.util.Optional.empty()

        When("해당 ID로 조회하면") {
            Then("VenueNotFoundException이 발생한다") {
                shouldThrow<VenueNotFoundException> {
                    getVenue(999L)
                }
            }
        }
    }
})
