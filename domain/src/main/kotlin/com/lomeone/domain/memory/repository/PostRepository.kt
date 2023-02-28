package com.lomeone.domain.memory.repository

import com.lomeone.domain.memory.entity.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository : JpaRepository<Post, Long> {
}
