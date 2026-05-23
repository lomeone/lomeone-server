package com.lomeone.moyemap.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Location(
    name: String,
    address: String,
    latitude: Double,
    longitude: Double,
    region: String,
    city: String? = null,
    district: String? = null
) {
    @Column(name = "location_name")
    var name: String = name.trim()
        protected set

    @Column(name = "location_address")
    var address: String = address.trim()
        protected set

    @Column(name = "location_latitude")
    var latitude: Double = latitude
        protected set

    @Column(name = "location_longitude")
    var longitude: Double = longitude
        protected set

    @Column(name = "location_region")
    var region: String = region.trim()
        protected set

    @Column(name = "location_city")
    var city: String? = city?.trim()?.ifBlank { null }
        protected set

    @Column(name = "location_district")
    var district: String? = district?.trim()?.ifBlank { null }
        protected set

    init {
        validate()
    }

    fun update(
        name: String? = null,
        address: String? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        region: String? = null,
        city: String? = null,
        district: String? = null
    ) {
        name?.let { this.name = it.trim() }
        address?.let { this.address = it.trim() }
        latitude?.let { this.latitude = it }
        longitude?.let { this.longitude = it }
        region?.let { this.region = it.trim() }
        city?.let { this.city = it.trim().ifBlank { null } }
        district?.let { this.district = it.trim().ifBlank { null } }
        validate()
    }

    private fun validate() {
        require(name.isNotBlank()) { "Location name must not be blank" }
        require(address.isNotBlank()) { "Location address must not be blank" }
        require(region.isNotBlank()) { "Location region must not be blank" }
        require(latitude in MIN_LATITUDE..MAX_LATITUDE) {
            "Location latitude must be between $MIN_LATITUDE and $MAX_LATITUDE"
        }
        require(longitude in MIN_LONGITUDE..MAX_LONGITUDE) {
            "Location longitude must be between $MIN_LONGITUDE and $MAX_LONGITUDE"
        }
    }

    companion object {
        private const val MIN_LATITUDE = -90.0
        private const val MAX_LATITUDE = 90.0
        private const val MIN_LONGITUDE = -180.0
        private const val MAX_LONGITUDE = 180.0
    }
}
