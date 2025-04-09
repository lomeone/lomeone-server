package com.lomeone.domain.authentication.repository

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.entity.AuthProvider
import org.springframework.data.jpa.repository.JpaRepository

interface AuthenticationRepository : JpaRepository<Authentication, Long> {
    fun findByUid(uid: String): Authentication?

    fun findByEmailAndProvider(email: String, provider: AuthProvider): Authentication?

    fun findByEmailAndPassword(email: String, password: String): Authentication?
}
