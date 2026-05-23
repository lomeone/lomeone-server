package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.entity.VenueStatus
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.VenueRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Optional

class HideVenueTest : BehaviorSpec({
    val venueRepository = mockk<VenueRepository>()
    val hideVenue = HideVenue(venueRepository)

    val location = Location("홍대", "서울 마포구", 37.5571, 126.9243, "홍대/연남")

    Given("PUBLISHED 상태의 Venue가 주어졌을 때") {
        val venue = Venue("소셜파티", VenueCategory.SOCIAL_PARTY, location, 25000, imageUrl = "img.jpg", description = "desc", sourceUrl = "source").apply {
            publish()
        }

        every { venueRepository.findById(1L) } returns Optional.of(venue)
        every { venueRepository.save(any()) } answers { firstArg() }

        When("숨기면") {
            val result = hideVenue(1L)

            Then("상태가 HIDDEN으로 변경된다") {
                result.status shouldBe VenueStatus.HIDDEN
                verify { venueRepository.save(venue) }
            }
        }
    }

    Given("존재하지 않는 Venue ID가 주어졌을 때") {
        every { venueRepository.findById(999L) } returns Optional.empty()

        When("숨기려고 하면") {
            Then("VenueNotFoundException이 발생한다") {
                shouldThrow<VenueNotFoundException> {
                    hideVenue(999L)
                }
            }
        }
    }
})
