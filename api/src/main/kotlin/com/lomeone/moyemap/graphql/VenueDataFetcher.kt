package com.lomeone.moyemap.graphql

import com.lomeone.generated.types.GetVenueMarkersInput
import com.lomeone.generated.types.GetVenuesInput
import com.lomeone.generated.types.Location
import com.lomeone.generated.types.SubmitVenueInput
import com.lomeone.generated.types.SubmitVenuePayload
import com.lomeone.generated.types.Venue
import com.lomeone.generated.types.VenueCategory
import com.lomeone.generated.types.VenueMarker
import com.lomeone.generated.types.VenueStatus
import com.lomeone.moyemap.exception.VenueNotFoundException
import com.lomeone.moyemap.service.GetVenue
import com.lomeone.moyemap.service.GetVenueMarkers
import com.lomeone.moyemap.service.GetVenueMarkersQuery
import com.lomeone.moyemap.service.GetVenues
import com.lomeone.moyemap.service.GetVenuesQuery
import com.lomeone.moyemap.service.SubmitVenue
import com.lomeone.moyemap.service.SubmitVenueCommand
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class VenueDataFetcher(
    private val getVenues: GetVenues,
    private val getVenue: GetVenue,
    private val getVenueMarkers: GetVenueMarkers,
    private val submitVenue: SubmitVenue
) {
    @DgsQuery
    fun venues(@InputArgument input: GetVenuesInput): List<Venue> {
        return getVenues(GetVenuesQuery(
            categories = input.categories?.map { it.toEntity() },
            region = input.region,
            minPrice = input.minPrice,
            maxPrice = input.maxPrice,
            northEastLatitude = input.northEastLatitude.toDouble(),
            northEastLongitude = input.northEastLongitude.toDouble(),
            southWestLatitude = input.southWestLatitude.toDouble(),
            southWestLongitude = input.southWestLongitude.toDouble()
        )).map { it.toGraphQL() }
    }

    @DgsQuery
    fun venue(@InputArgument id: String): Venue? {
        return try {
            getVenue(id.toLong()).toGraphQL()
        } catch (e: VenueNotFoundException) {
            null
        }
    }

    @DgsQuery
    fun venueMarkers(@InputArgument input: GetVenueMarkersInput): List<VenueMarker> {
        return getVenueMarkers(GetVenueMarkersQuery(
            northEastLatitude = input.northEastLatitude.toDouble(),
            northEastLongitude = input.northEastLongitude.toDouble(),
            southWestLatitude = input.southWestLatitude.toDouble(),
            southWestLongitude = input.southWestLongitude.toDouble(),
            categories = input.categories?.map { it.toEntity() },
            region = input.region
        )).map { it.toMarkerGraphQL() }
    }

    @DgsMutation
    fun submitVenue(@InputArgument input: SubmitVenueInput): SubmitVenuePayload {
        val venue = submitVenue(SubmitVenueCommand(
            title = input.title,
            category = input.category.toEntity(),
            minPrice = input.minPrice,
            sourceUrl = input.sourceUrl,
            locationName = input.locationName,
            locationAddress = input.locationAddress,
            locationRegion = input.locationRegion,
            imageUrl = input.imageUrl,
            tags = input.tags,
            description = input.description
        ))
        return SubmitVenuePayload(
            id = venue.id.toString(),
            title = venue.title,
            status = venue.status.toGraphQL()
        )
    }

    private fun com.lomeone.moyemap.entity.Venue.toGraphQL() = Venue(
        id = this.id.toString(),
        title = this.title,
        category = this.category.toGraphQL(),
        status = this.status.toGraphQL(),
        location = this.location.toGraphQL(),
        minPrice = this.minPrice,
        currency = this.currency,
        imageUrl = this.imageUrl,
        description = this.description,
        sourceUrl = this.sourceUrl,
        tags = this.tags,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )

    private fun com.lomeone.moyemap.entity.Venue.toMarkerGraphQL() = VenueMarker(
        id = this.id.toString(),
        title = this.title,
        category = this.category.toGraphQL(),
        minPrice = this.minPrice,
        latitude = this.location.latitude,
        longitude = this.location.longitude,
        region = this.location.region
    )

    private fun com.lomeone.moyemap.entity.Location.toGraphQL() = Location(
        name = this.name,
        address = this.address,
        latitude = this.latitude,
        longitude = this.longitude,
        region = this.region,
        city = this.city,
        district = this.district
    )

    private fun com.lomeone.moyemap.entity.VenueCategory.toGraphQL() = when (this) {
        com.lomeone.moyemap.entity.VenueCategory.SOCIAL_PARTY -> VenueCategory.SOCIAL_PARTY
        com.lomeone.moyemap.entity.VenueCategory.SOLO_PARTY -> VenueCategory.SOLO_PARTY
        com.lomeone.moyemap.entity.VenueCategory.GUESTHOUSE_PARTY -> VenueCategory.GUESTHOUSE_PARTY
        com.lomeone.moyemap.entity.VenueCategory.ROTATION_DATING -> VenueCategory.ROTATION_DATING
        com.lomeone.moyemap.entity.VenueCategory.NETWORKING -> VenueCategory.NETWORKING
        com.lomeone.moyemap.entity.VenueCategory.HONSOOL_BAR -> VenueCategory.HONSOOL_BAR
        com.lomeone.moyemap.entity.VenueCategory.BAR -> VenueCategory.BAR
        com.lomeone.moyemap.entity.VenueCategory.WORKSHOP -> VenueCategory.WORKSHOP
        com.lomeone.moyemap.entity.VenueCategory.ETC -> VenueCategory.ETC
    }

    private fun com.lomeone.moyemap.entity.VenueStatus.toGraphQL() = when (this) {
        com.lomeone.moyemap.entity.VenueStatus.DRAFT -> VenueStatus.DRAFT
        com.lomeone.moyemap.entity.VenueStatus.PUBLISHED -> VenueStatus.PUBLISHED
        com.lomeone.moyemap.entity.VenueStatus.HIDDEN -> VenueStatus.HIDDEN
    }

    private fun VenueCategory.toEntity() = when (this) {
        VenueCategory.SOCIAL_PARTY -> com.lomeone.moyemap.entity.VenueCategory.SOCIAL_PARTY
        VenueCategory.SOLO_PARTY -> com.lomeone.moyemap.entity.VenueCategory.SOLO_PARTY
        VenueCategory.GUESTHOUSE_PARTY -> com.lomeone.moyemap.entity.VenueCategory.GUESTHOUSE_PARTY
        VenueCategory.ROTATION_DATING -> com.lomeone.moyemap.entity.VenueCategory.ROTATION_DATING
        VenueCategory.NETWORKING -> com.lomeone.moyemap.entity.VenueCategory.NETWORKING
        VenueCategory.HONSOOL_BAR -> com.lomeone.moyemap.entity.VenueCategory.HONSOOL_BAR
        VenueCategory.BAR -> com.lomeone.moyemap.entity.VenueCategory.BAR
        VenueCategory.WORKSHOP -> com.lomeone.moyemap.entity.VenueCategory.WORKSHOP
        VenueCategory.ETC -> com.lomeone.moyemap.entity.VenueCategory.ETC
    }
}
