package com.lomeone.domain.post.repository

import com.lomeone.domain.post.entity.Photo
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<Photo, Long>
