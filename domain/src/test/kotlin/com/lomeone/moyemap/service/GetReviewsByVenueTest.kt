package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Review
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.ReviewRepository
import com.lomeone.moyemap.repository.VenueRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class GetReviewsByVenueTest : BehaviorSpec({
    val reviewRepository = mockk<ReviewRepository>()
    val venueRepository = mockk<VenueRepository>()
    val getReviewsByVenue = GetReviewsByVenue(reviewRepository, venueRepository)

    val location = Location("홍대", "서울 마포구", 37.5571, 126.9243, "홍대/연남")
    val venue = Venue("소셜파티", VenueCategory.SOCIAL_PARTY, location, 25000, imageUrl = "img", description = "desc", sourceUrl = "url")

    val review1 = Review(venue, "user1", 5, "Great!")
    val review2 = Review(venue, "user2", 4, "Good")
    val review3 = Review(venue, "user3", 3, "OK")

    Given("특정 Venue의 모든 리뷰를 조회할 때") {
        every { venueRepository.findById(1L) } returns java.util.Optional.of(venue)
        every { reviewRepository.findByVenue(venue) } returns listOf(review1, review2, review3)

        When("최소 평점 필터 없이 조회하면") {
            val query = GetReviewsByVenueQuery(venueId = 1L)
            val result = getReviewsByVenue(query)

            Then("모든 리뷰가 반환된다") {
                result shouldHaveSize 3
                verify {
                    venueRepository.findById(1L)
                    reviewRepository.findByVenue(venue)
                }
            }
        }
    }

    Given("최소 평점 필터가 주어졌을 때") {
        every { venueRepository.findById(1L) } returns java.util.Optional.of(venue)
        every { reviewRepository.findByVenue(venue) } returns listOf(review1, review2, review3)

        When("최소 평점 이상의 리뷰만 조회하면") {
            val query = GetReviewsByVenueQuery(venueId = 1L, minRating = 4)
            val result = getReviewsByVenue(query)

            Then("조건을 만족하는 리뷰만 반환된다") {
                result shouldHaveSize 2
                result.all { it.rating >= 4 } shouldBe true
            }
        }
    }

    Given("존재하지 않는 Venue ID가 주어졌을 때") {
        every { venueRepository.findById(999L) } returns java.util.Optional.empty()

        When("리뷰를 조회하려고 하면") {
            val query = GetReviewsByVenueQuery(venueId = 999L)

            Then("VenueNotFoundException이 발생한다") {
                shouldThrow<VenueNotFoundException> {
                    getReviewsByVenue(query)
                }
            }
        }
    }
})
