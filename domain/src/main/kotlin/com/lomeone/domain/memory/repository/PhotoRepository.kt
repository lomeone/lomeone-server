package com.lomeone.domain.memory.repository

import com.lomeone.domain.memory.entity.Photo
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<Photo, Long>
