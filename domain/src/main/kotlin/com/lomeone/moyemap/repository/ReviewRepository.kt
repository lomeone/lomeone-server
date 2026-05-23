package com.lomeone.moyemap.repository

import com.lomeone.moyemap.entity.Review
import com.lomeone.moyemap.entity.Venue
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findByVenue(venue: Venue): List<Review>
    fun countByVenue(venue: Venue): Long
}
