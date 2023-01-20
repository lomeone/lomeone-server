package io.github.comstering.memory.repository

import io.github.comstering.memory.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
}
