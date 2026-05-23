package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.entity.VenueStatus
import com.lomeone.moyemap.repository.VenueRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import com.lomeone.moyemap.common.MoyemapTransactional

@Service
class GetVenues(
    private val venueRepository: VenueRepository
) {
    private val logger = KotlinLogging.logger {}

    @MoyemapTransactional(readOnly = true)
    operator fun invoke(query: GetVenuesQuery = GetVenuesQuery()): List<Venue> {
        logger.info { "Getting venues: categories=${query.categories}, region=${query.region}, minPrice=${query.minPrice}, maxPrice=${query.maxPrice}, hasBounds=${query.hasBounds()}" }

        val venues = if (query.hasBounds()) {
            venueRepository.findPublishedInBounds(
                swLat = query.southWestLatitude!!,
                neLat = query.northEastLatitude!!,
                swLng = query.southWestLongitude!!,
                neLng = query.northEastLongitude!!
            )
        } else {
            venueRepository.findByStatus(VenueStatus.PUBLISHED)
        }

        val result = venues
            .filter { query.categories == null || it.category in query.categories }
            .filter { query.region == null || it.location.region == query.region }
            .filter {
                when {
                    query.minPrice != null && query.maxPrice != null -> it.price in query.minPrice..query.maxPrice
                    query.minPrice != null -> it.price >= query.minPrice
                    query.maxPrice != null -> it.price <= query.maxPrice
                    else -> true
                }
            }

        logger.info { "Venues found: count=${result.size}" }
        return result
    }
}

data class GetVenuesQuery(
    val categories: List<VenueCategory>? = null,
    val region: String? = null,
    val minPrice: Int? = null,
    val maxPrice: Int? = null,
    val northEastLatitude: Double? = null,
    val northEastLongitude: Double? = null,
    val southWestLatitude: Double? = null,
    val southWestLongitude: Double? = null
) {
    fun hasBounds() = northEastLatitude != null && northEastLongitude != null &&
        southWestLatitude != null && southWestLongitude != null
}
