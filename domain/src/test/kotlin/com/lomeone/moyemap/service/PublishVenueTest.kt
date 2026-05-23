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

class PublishVenueTest : BehaviorSpec({
    val venueRepository = mockk<VenueRepository>()
    val publishVenue = PublishVenue(venueRepository)

    val location = Location("홍대", "서울 마포구", 37.5571, 126.9243, "홍대/연남")

    Given("DRAFT 상태의 Venue가 주어졌을 때") {
        val venue = Venue("소셜파티", VenueCategory.SOCIAL_PARTY, location, 25000, imageUrl = "img.jpg", description = "desc", sourceUrl = "source")

        every { venueRepository.findById(1L) } returns Optional.of(venue)
        every { venueRepository.save(any()) } answers { firstArg() }

        When("게시하면") {
            val result = publishVenue(1L)

            Then("상태가 PUBLISHED로 변경된다") {
                result.status shouldBe VenueStatus.PUBLISHED
                verify { venueRepository.save(venue) }
            }
        }
    }

    Given("HIDDEN 상태의 Venue가 주어졌을 때") {
        val venue = Venue("소셜파티", VenueCategory.SOCIAL_PARTY, location, 25000, imageUrl = "img.jpg", description = "desc", sourceUrl = "source").apply {
            publish()
            hide()
        }

        every { venueRepository.findById(2L) } returns Optional.of(venue)
        every { venueRepository.save(any()) } answers { firstArg() }

        When("게시하면") {
            val result = publishVenue(2L)

            Then("상태가 PUBLISHED로 변경된다") {
                result.status shouldBe VenueStatus.PUBLISHED
            }
        }
    }

    Given("존재하지 않는 Venue ID가 주어졌을 때") {
        every { venueRepository.findById(999L) } returns Optional.empty()

        When("게시하려고 하면") {
            Then("VenueNotFoundException이 발생한다") {
                shouldThrow<VenueNotFoundException> {
                    publishVenue(999L)
                }
            }
        }
    }
})
