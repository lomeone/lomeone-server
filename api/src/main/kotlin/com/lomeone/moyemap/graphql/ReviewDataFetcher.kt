package com.lomeone.moyemap.graphql

import com.lomeone.generated.types.CreateReviewInput
import com.lomeone.generated.types.CreateReviewPayload
import com.lomeone.generated.types.Review
import com.lomeone.moyemap.service.CreateReview
import com.lomeone.moyemap.service.CreateReviewCommand
import com.lomeone.moyemap.service.GetReviewsByVenue
import com.lomeone.moyemap.service.GetReviewsByVenueQuery
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class ReviewDataFetcher(
    private val createReview: CreateReview,
    private val getReviewsByVenue: GetReviewsByVenue
) {
    @DgsQuery
    fun reviewsByVenue(
        @InputArgument venueId: String,
        @InputArgument minRating: Int?
    ): List<Review> {
        return getReviewsByVenue(GetReviewsByVenueQuery(
            venueId = venueId.toLong(),
            minRating = minRating
        )).map { it.toGraphQL() }
    }

    @DgsMutation
    fun createReview(@InputArgument input: CreateReviewInput): CreateReviewPayload {
        val result = createReview(CreateReviewCommand(
            venueId = input.venueId.toLong(),
            userName = input.userName,
            rating = input.rating,
            comment = input.comment
        ))
        return CreateReviewPayload(
            review = Review(
                id = result.id.toString(),
                venueId = result.venueId.toString(),
                userName = result.userName,
                rating = result.rating,
                comment = result.comment,
                createdAt = result.createdAt.toString(),
                updatedAt = result.createdAt.toString()
            )
        )
    }

    private fun com.lomeone.moyemap.entity.Review.toGraphQL() = Review(
        id = this.id.toString(),
        venueId = this.venue.id.toString(),
        userName = this.userName,
        rating = this.rating,
        comment = this.comment,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}
