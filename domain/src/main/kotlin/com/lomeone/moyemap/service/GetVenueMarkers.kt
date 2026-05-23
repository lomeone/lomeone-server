package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.repository.VenueRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import com.lomeone.moyemap.common.MoyemapTransactional

@Service
class GetVenueMarkers(
    private val venueRepository: VenueRepository
) {
    private val logger = KotlinLogging.logger {}

    @MoyemapTransactional(readOnly = true)
    operator fun invoke(query: GetVenueMarkersQuery = GetVenueMarkersQuery()): List<Venue> {
        logger.info { "Getting venue markers: region=${query.region}, categories=${query.categories}, bounds=[${query.southWestLatitude},${query.southWestLongitude}]~[${query.northEastLatitude},${query.northEastLongitude}]" }

        val markers = venueRepository.findPublishedInBounds(
            swLat = query.southWestLatitude ?: -90.0,
            neLat = query.northEastLatitude ?: 90.0,
            swLng = query.southWestLongitude ?: -180.0,
            neLng = query.northEastLongitude ?: 180.0
        ).filter { query.categories == null || it.category in query.categories }
         .filter { query.region == null || it.location.region == query.region }

        logger.info { "Venue markers found: count=${markers.size}" }
        return markers
    }
}

data class GetVenueMarkersQuery(
    val northEastLatitude: Double? = null,
    val northEastLongitude: Double? = null,
    val southWestLatitude: Double? = null,
    val southWestLongitude: Double? = null,
    val categories: List<VenueCategory>? = null,
    val region: String? = null
)
