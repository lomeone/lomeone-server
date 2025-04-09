package com.lomeone.domain.user.repository

import com.lomeone.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserToken(userToken: String): User?

    fun findByEmail(email: String): User?

    fun findByPhoneNumber(phoneNumber: String): User?
}
