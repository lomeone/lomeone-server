package com.lomeone.user.repository

import com.lomeone.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<com.lomeone.user.entity.User, Long> {
    fun findByUserToken(userToken: String): com.lomeone.user.entity.User?

    fun findByEmail(email: String): com.lomeone.user.entity.User?

    fun findByPhoneNumber(phoneNumber: String): com.lomeone.user.entity.User?
}
