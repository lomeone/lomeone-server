package com.lomeone.moyemap.graphql

import com.lomeone.generated.types.CreateVenueInput
import com.lomeone.generated.types.CreateVenuePayload
import com.lomeone.generated.types.GetVenueMarkersInput
import com.lomeone.generated.types.GetVenuesInput
import com.lomeone.generated.types.HideVenuePayload
import com.lomeone.generated.types.Location
import com.lomeone.generated.types.PublishVenuePayload
import com.lomeone.generated.types.UpdateVenueInput
import com.lomeone.generated.types.UpdateVenuePayload
import com.lomeone.generated.types.Venue
import com.lomeone.generated.types.VenueCategory
import com.lomeone.generated.types.VenueMarker
import com.lomeone.generated.types.VenueStatus
import com.lomeone.moyemap.service.CreateVenue
import com.lomeone.moyemap.service.CreateVenueCommand
import com.lomeone.moyemap.service.GetVenue
import com.lomeone.moyemap.service.GetVenueMarkers
import com.lomeone.moyemap.service.GetVenueMarkersQuery
import com.lomeone.moyemap.service.GetVenues
import com.lomeone.moyemap.service.GetVenuesQuery
import com.lomeone.moyemap.service.HideVenue
import com.lomeone.moyemap.service.PublishVenue
import com.lomeone.moyemap.service.UpdateVenue
import com.lomeone.moyemap.service.UpdateVenueCommand
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class VenueDataFetcher(
    private val getVenues: GetVenues,
    private val getVenue: GetVenue,
    private val getVenueMarkers: GetVenueMarkers,
    private val createVenue: CreateVenue,
    private val updateVenue: UpdateVenue,
    private val publishVenue: PublishVenue,
    private val hideVenue: HideVenue
) {
    @DgsQuery
    fun venues(@InputArgument input: GetVenuesInput?): List<Venue> {
        return getVenues(GetVenuesQuery(
            categories = input?.categories?.map { it.toEntity() },
            region = input?.region,
            minPrice = input?.minPrice,
            maxPrice = input?.maxPrice,
            northEastLatitude = input?.northEastLatitude?.toDouble(),
            northEastLongitude = input?.northEastLongitude?.toDouble(),
            southWestLatitude = input?.southWestLatitude?.toDouble(),
            southWestLongitude = input?.southWestLongitude?.toDouble()
        )).map { it.toGraphQL() }
    }

    @DgsQuery
    fun venue(@InputArgument id: String): Venue {
        return getVenue(id.toLong()).toGraphQL()
    }

    @DgsQuery
    fun venueMarkers(@InputArgument input: GetVenueMarkersInput?): List<VenueMarker> {
        return getVenueMarkers(GetVenueMarkersQuery(
            northEastLatitude = input?.northEastLatitude?.toDouble(),
            northEastLongitude = input?.northEastLongitude?.toDouble(),
            southWestLatitude = input?.southWestLatitude?.toDouble(),
            southWestLongitude = input?.southWestLongitude?.toDouble(),
            categories = input?.categories?.map { it.toEntity() },
            region = input?.region
        )).map { it.toMarkerGraphQL() }
    }

    @DgsMutation
    fun createVenue(@InputArgument input: CreateVenueInput): CreateVenuePayload {
        val venue = createVenue(CreateVenueCommand(
            title = input.title,
            category = input.category.toEntity(),
            locationName = input.locationName,
            address = input.address,
            latitude = input.latitude.toDouble(),
            longitude = input.longitude.toDouble(),
            region = input.region,
            city = input.city,
            district = input.district,
            price = input.price,
            currency = input.currency ?: "KRW",
            imageUrl = input.imageUrl,
            description = input.description,
            sourceUrl = input.sourceUrl,
            tags = input.tags ?: emptyList()
        ))
        return CreateVenuePayload(venue = venue.toGraphQL())
    }

    @DgsMutation
    fun updateVenue(@InputArgument input: UpdateVenueInput): UpdateVenuePayload {
        val venue = updateVenue(UpdateVenueCommand(
            venueId = input.id.toLong(),
            title = input.title,
            category = input.category?.toEntity(),
            locationName = input.locationName,
            address = input.address,
            latitude = input.latitude?.toDouble(),
            longitude = input.longitude?.toDouble(),
            region = input.region,
            city = input.city,
            district = input.district,
            price = input.price,
            currency = input.currency,
            imageUrl = input.imageUrl,
            description = input.description,
            sourceUrl = input.sourceUrl,
            tags = input.tags
        ))
        return UpdateVenuePayload(venue = venue.toGraphQL())
    }

    @DgsMutation
    fun publishVenue(@InputArgument id: String): PublishVenuePayload {
        return PublishVenuePayload(venue = publishVenue(id.toLong()).toGraphQL())
    }

    @DgsMutation
    fun hideVenue(@InputArgument id: String): HideVenuePayload {
        return HideVenuePayload(venue = hideVenue(id.toLong()).toGraphQL())
    }

    private fun com.lomeone.moyemap.entity.Venue.toGraphQL() = Venue(
        id = this.id.toString(),
        title = this.title,
        category = this.category.toGraphQL(),
        status = this.status.toGraphQL(),
        location = this.location.toGraphQL(),
        price = this.price,
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
        price = this.price,
        currency = this.currency,
        latitude = this.location.latitude,
        longitude = this.location.longitude,
        region = this.location.region,
        imageUrl = this.imageUrl,
        tags = this.tags
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

    private fun VenueStatus.toEntity() = when (this) {
        VenueStatus.DRAFT -> com.lomeone.moyemap.entity.VenueStatus.DRAFT
        VenueStatus.PUBLISHED -> com.lomeone.moyemap.entity.VenueStatus.PUBLISHED
        VenueStatus.HIDDEN -> com.lomeone.moyemap.entity.VenueStatus.HIDDEN
    }
}
