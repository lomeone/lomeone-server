package com.lomeone.moyemap.entity

import com.lomeone.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(
    name = "reviews",
    indexes = [
        Index(name = "idx_reviews_venue_id", columnList = "venue_id"),
        Index(name = "idx_reviews_rating", columnList = "rating")
    ]
)
class Review(
    venue: Venue,
    userName: String,
    rating: Int,
    comment: String
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    val id: Long = 0L

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    var venue: Venue = venue
        protected set

    var userName: String = userName
        protected set

    var rating: Int = rating
        protected set

    @Column(columnDefinition = "TEXT")
    var comment: String = comment
        protected set

    init {
        require(rating in 1..5) { "Rating must be between 1 and 5" }
    }

    fun updateReview(
        userName: String? = null,
        rating: Int? = null,
        comment: String? = null
    ) {
        userName?.let { this.userName = it }
        rating?.let {
            require(it in 1..5) { "Rating must be between 1 and 5" }
            this.rating = it
        }
        comment?.let { this.comment = it }
    }
}
