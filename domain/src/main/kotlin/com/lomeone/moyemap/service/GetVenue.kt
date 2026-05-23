package com.lomeone.moyemap.service

import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.repository.VenueRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.moyemap.common.MoyemapTransactional

@Service
class GetVenue(
    private val venueRepository: VenueRepository
) {
    private val logger = KotlinLogging.logger {}

    @MoyemapTransactional(readOnly = true)
    operator fun invoke(venueId: Long): Venue {
        logger.info { "Getting venue: id=$venueId" }
        return venueRepository.findByIdOrNull(venueId)
            ?: throw VenueNotFoundException(detail = mapOf("venueId" to venueId))
    }
}
