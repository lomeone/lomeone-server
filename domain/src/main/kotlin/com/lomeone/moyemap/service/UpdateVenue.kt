package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.VenueRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.moyemap.common.MoyemapTransactional

@Service
class UpdateVenue(
    private val venueRepository: VenueRepository
) {
    private val logger = KotlinLogging.logger {}

    @MoyemapTransactional
    operator fun invoke(command: UpdateVenueCommand): Venue {
        logger.info { "Updating venue: id=${command.venueId}" }

        val venue = venueRepository.findByIdOrNull(command.venueId)
            ?: throw VenueNotFoundException(detail = mapOf("venueId" to command.venueId))

        venue.update(
            title = command.title,
            category = command.category,
            price = command.price,
            currency = command.currency,
            imageUrl = command.imageUrl,
            description = command.description,
            sourceUrl = command.sourceUrl,
            tags = command.tags
        )

        if (command.locationName != null || command.address != null ||
            command.latitude != null || command.longitude != null || command.region != null) {
            val cur = venue.location
            venue.updateLocation(Location(
                name = command.locationName ?: cur.name,
                address = command.address ?: cur.address,
                latitude = command.latitude ?: cur.latitude,
                longitude = command.longitude ?: cur.longitude,
                region = command.region ?: cur.region,
                city = command.city ?: cur.city,
                district = command.district ?: cur.district
            ))
        }

        val saved = venueRepository.save(venue)
        logger.info { "Venue updated: id=${saved.id}" }
        return saved
    }
}

data class UpdateVenueCommand(
    val venueId: Long,
    val title: String? = null,
    val category: VenueCategory? = null,
    val locationName: String? = null,
    val address: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val region: String? = null,
    val city: String? = null,
    val district: String? = null,
    val price: Int? = null,
    val currency: String? = null,
    val imageUrl: String? = null,
    val description: String? = null,
    val sourceUrl: String? = null,
    val tags: List<String>? = null
)
