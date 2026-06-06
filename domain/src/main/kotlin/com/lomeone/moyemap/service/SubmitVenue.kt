package com.lomeone.moyemap.service

import com.lomeone.moyemap.common.MoyemapTransactional
import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.repository.VenueRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service

@Service
class SubmitVenue(
    private val venueRepository: VenueRepository
) {
    private val logger = KotlinLogging.logger {}

    @MoyemapTransactional
    operator fun invoke(command: SubmitVenueCommand): Venue {
        logger.info { "Submitting venue: title=${command.title}, category=${command.category}, region=${command.locationRegion}" }

        val location = Location(
            name = command.locationName,
            address = command.locationAddress,
            latitude = 0.0,
            longitude = 0.0,
            region = command.locationRegion
        )

        val venue = Venue(
            title = command.title,
            category = command.category,
            location = location,
            minPrice = command.minPrice,
            imageUrl = command.imageUrl ?: "",
            description = command.description,
            sourceUrl = command.sourceUrl,
            tags = command.tags
        )

        val saved = venueRepository.save(venue)
        logger.info { "Venue submitted: id=${saved.id}, title=${saved.title}" }
        return saved
    }
}

data class SubmitVenueCommand(
    val title: String,
    val category: VenueCategory,
    val minPrice: Int? = null,
    val sourceUrl: String,
    val locationName: String,
    val locationAddress: String,
    val locationRegion: String,
    val imageUrl: String? = null,
    val tags: List<String> = emptyList(),
    val description: String
)
