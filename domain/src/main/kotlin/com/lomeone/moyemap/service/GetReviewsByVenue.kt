package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Review
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.ReviewRepository
import com.lomeone.moyemap.repository.VenueRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.moyemap.common.MoyemapTransactional

@Service
class GetReviewsByVenue(
    private val reviewRepository: ReviewRepository,
    private val venueRepository: VenueRepository
) {
    @MoyemapTransactional(readOnly = true)
    operator fun invoke(query: GetReviewsByVenueQuery): List<Review> {
        val venue = venueRepository.findByIdOrNull(query.venueId)
            ?: throw VenueNotFoundException(detail = mapOf("venueId" to query.venueId))

        val reviews = reviewRepository.findByVenue(venue)

        return if (query.minRating != null) {
            reviews.filter { it.rating >= query.minRating }
        } else {
            reviews
        }
    }
}

data class GetReviewsByVenueQuery(
    val venueId: Long,
    val minRating: Int? = null
)
