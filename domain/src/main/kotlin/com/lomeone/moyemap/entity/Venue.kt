package com.lomeone.moyemap.entity

import com.lomeone.common.entity.AuditEntity
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "venues",
    indexes = [
        Index(name = "idx_venues_category", columnList = "category"),
        Index(name = "idx_venues_status", columnList = "status"),
        Index(name = "idx_venues_region", columnList = "location_region")
    ]
)
class Venue(
    title: String,
    category: VenueCategory,
    location: Location,
    price: Int,
    currency: String = "KRW",
    imageUrl: String,
    description: String,
    sourceUrl: String,
    tags: List<String> = emptyList()
) : AuditEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "venue_id")
    val id: Long = 0L

    var title: String = title
        protected set

    @Enumerated(EnumType.STRING)
    var category: VenueCategory = category
        protected set

    @Embedded
    var location: Location = location
        protected set

    var price: Int = price
        protected set

    var currency: String = currency
        protected set

    var imageUrl: String = imageUrl
        protected set

    @Column(columnDefinition = "TEXT")
    var description: String = description
        protected set

    var sourceUrl: String = sourceUrl
        protected set

    @Convert(converter = VenueTagsConverter::class)
    @Column(columnDefinition = "TEXT")
    var tags: List<String> = tags
        protected set

    @Enumerated(EnumType.STRING)
    var status: VenueStatus = VenueStatus.DRAFT
        protected set

    init {
        require(title.isNotBlank()) { "Venue title must not be blank" }
        require(price >= 0) { "Venue price must be non-negative" }
    }

    fun publish() {
        check(status != VenueStatus.PUBLISHED) { "Venue is already published" }
        status = VenueStatus.PUBLISHED
    }

    fun hide() {
        check(status == VenueStatus.PUBLISHED) { "Only published venues can be hidden" }
        status = VenueStatus.HIDDEN
    }

    fun update(
        title: String? = null,
        category: VenueCategory? = null,
        price: Int? = null,
        currency: String? = null,
        imageUrl: String? = null,
        description: String? = null,
        sourceUrl: String? = null,
        tags: List<String>? = null
    ) {
        title?.let { require(it.isNotBlank()) { "Venue title must not be blank" }; this.title = it }
        category?.let { this.category = it }
        price?.let { require(it >= 0) { "Venue price must be non-negative" }; this.price = it }
        currency?.let { this.currency = it }
        imageUrl?.let { this.imageUrl = it }
        description?.let { this.description = it }
        sourceUrl?.let { this.sourceUrl = it }
        tags?.let { this.tags = it }
    }

    fun updateLocation(location: Location) {
        this.location = location
    }
}

enum class VenueCategory {
    SOCIAL_PARTY,
    SOLO_PARTY,
    GUESTHOUSE_PARTY,
    ROTATION_DATING,
    NETWORKING,
    HONSOOL_BAR,
    BAR,
    WORKSHOP,
    ETC
}

enum class VenueStatus {
    DRAFT,
    PUBLISHED,
    HIDDEN
}
