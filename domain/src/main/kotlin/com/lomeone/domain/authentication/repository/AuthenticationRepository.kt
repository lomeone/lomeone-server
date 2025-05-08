package com.lomeone.domain.authentication.repository

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Realm
import org.springframework.data.jpa.repository.JpaRepository

interface AuthenticationRepository : JpaRepository<Authentication, Long> {
    fun findByUid(uid: String): Authentication?

    fun findByEmailAndProvider(email: String, provider: AuthProvider): Authentication?

    fun findByEmailAndProviderAndRealm(email: String, provider: AuthProvider, realm: Realm): Authentication?

    fun findByEmailAndPassword(email: String, password: String): Authentication?

    fun findByUidAndRealm(uid: String, realm: Realm): Authentication?
}
