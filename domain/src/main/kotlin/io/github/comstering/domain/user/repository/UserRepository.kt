package io.github.comstering.domain.user.repository

import io.github.comstering.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByFirebaseUserToken(firebaseUserToken: String): User?
}
