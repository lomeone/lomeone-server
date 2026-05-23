package com.lomeone.moyemap.repository

import com.lomeone.moyemap.entity.Venue
import com.lomeone.moyemap.entity.VenueCategory
import com.lomeone.moyemap.entity.VenueStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface VenueRepository : JpaRepository<Venue, Long> {
    fun findByStatus(status: VenueStatus): List<Venue>
    fun findByCategoryAndStatus(category: VenueCategory, status: VenueStatus): List<Venue>

    @Query("""
        SELECT v FROM Venue v
        WHERE v.status = com.lomeone.moyemap.entity.VenueStatus.PUBLISHED
          AND v.location.latitude BETWEEN :swLat AND :neLat
          AND v.location.longitude BETWEEN :swLng AND :neLng
    """)
    fun findPublishedInBounds(
        @Param("swLat") swLat: Double,
        @Param("neLat") neLat: Double,
        @Param("swLng") swLng: Double,
        @Param("neLng") neLng: Double
    ): List<Venue>
}
