package io.github.comstering.domain.memory.repository

import io.github.comstering.domain.memory.entity.Photo
import org.springframework.data.jpa.repository.JpaRepository

interface PhotoRepository : JpaRepository<Photo, Long>
