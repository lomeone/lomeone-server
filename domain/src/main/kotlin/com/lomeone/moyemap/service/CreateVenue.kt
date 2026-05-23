package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Location
import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.repository.VenueRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import com.lomeone.moyemap.common.MoyemapTransactional

@Service
class CreateVenue(
    private val venueRepository: VenueRepository
) {
    private val logger = KotlinLogging.logger {}

    @MoyemapTransactional
    operator fun invoke(command: CreateVenueCommand): Venue {
        logger.info { "Creating venue: title=${command.title}, category=${command.category}, region=${command.region}, price=${command.price}" }

        val location = Location(
            name = command.locationName,
            address = command.address,
            latitude = command.latitude,
            longitude = command.longitude,
            region = command.region,
            city = command.city,
            district = command.district
        )

        val venue = Venue(
            title = command.title,
            category = command.category,
            location = location,
            price = command.price,
            currency = command.currency,
            imageUrl = command.imageUrl,
            description = command.description,
            sourceUrl = command.sourceUrl,
            tags = command.tags
        )

        val saved = venueRepository.save(venue)
        logger.info { "Venue created: id=${saved.id}, title=${saved.title}" }
        return saved
    }
}

data class CreateVenueCommand(
    val title: String,
    val category: VenueCategory,
    val locationName: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val region: String,
    val city: String? = null,
    val district: String? = null,
    val price: Int,
    val currency: String = "KRW",
    val imageUrl: String,
    val description: String,
    val sourceUrl: String,
    val tags: List<String> = emptyList()
)
