package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.ReviewRepository
import com.lomeone.moyemap.repository.VenueRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.util.Optional

class CreateReviewTest : BehaviorSpec({
    val reviewRepository = mockk<ReviewRepository>()
    val venueRepository = mockk<VenueRepository>()
    val createReview = CreateReview(reviewRepository, venueRepository)

    Given("존재하는 Venue가 주어졌을 때") {
        val location = Location("홍대", "서울 마포구", 37.5571, 126.9243, "홍대/연남")
        val venue = Venue("소셜파티", VenueCategory.SOCIAL_PARTY, location, 25000, imageUrl = "img", description = "desc", sourceUrl = "url")

        every { venueRepository.findById(1L) } returns Optional.of(venue)
        every { reviewRepository.save(any()) } answers { firstArg() }

        When("리뷰를 생성하면") {
            val command = CreateReviewCommand(
                venueId = 1L,
                userName = "김민수",
                rating = 5,
                comment = "분위기 너무 좋아요!"
            )

            val result = createReview(command)

            Then("리뷰가 생성되고 결과가 반환된다") {
                result.venueId shouldBe 1L
                result.userName shouldBe "김민수"
                result.rating shouldBe 5
                result.comment shouldBe "분위기 너무 좋아요!"

                verify {
                    reviewRepository.save(match { it.userName == "김민수" && it.rating == 5 })
                }
            }
        }
    }

    Given("존재하지 않는 Venue ID가 주어졌을 때") {
        every { venueRepository.findById(999L) } returns Optional.empty()

        When("리뷰를 생성하려고 하면") {
            val command = CreateReviewCommand(
                venueId = 999L,
                userName = "김민수",
                rating = 5,
                comment = "좋아요"
            )

            Then("VenueNotFoundException이 발생한다") {
                shouldThrow<VenueNotFoundException> {
                    createReview(command)
                }
            }
        }
    }
})
