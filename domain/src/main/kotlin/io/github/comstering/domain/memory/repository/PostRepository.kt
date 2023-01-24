package io.github.comstering.domain.memory.repository

import io.github.comstering.domain.memory.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
}
