package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Review
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.VenueRepository
import com.lomeone.moyemap.repository.ReviewRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.moyemap.common.MoyemapTransactional

@Service
class CreateReview(
    private val reviewRepository: ReviewRepository,
    private val venueRepository: VenueRepository
) {
    @MoyemapTransactional
    operator fun invoke(command: CreateReviewCommand): CreateReviewResult {
        val venue = venueRepository.findByIdOrNull(command.venueId)
            ?: throw VenueNotFoundException(detail = mapOf("venueId" to command.venueId))

        val review = Review(
            venue = venue,
            userName = command.userName,
            rating = command.rating,
            comment = command.comment
        )

        val savedReview = reviewRepository.save(review)

        return CreateReviewResult(
            id = savedReview.id,
            venueId = command.venueId,
            userName = savedReview.userName,
            rating = savedReview.rating,
            comment = savedReview.comment,
            createdAt = savedReview.createdAt
        )
    }
}

data class CreateReviewCommand(
    val venueId: Long,
    val userName: String,
    val rating: Int,
    val comment: String
)

data class CreateReviewResult(
    val id: Long,
    val venueId: Long,
    val userName: String,
    val rating: Int,
    val comment: String,
    val createdAt: java.time.LocalDateTime
)
