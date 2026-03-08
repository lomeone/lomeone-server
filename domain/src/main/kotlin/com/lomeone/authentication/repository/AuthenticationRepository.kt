package com.lomeone.authentication.repository

import com.lomeone.authentication.entity.Authentication
import com.lomeone.authentication.entity.AuthProvider
import com.lomeone.authentication.entity.Realm
import org.springframework.data.jpa.repository.JpaRepository

interface AuthenticationRepository : JpaRepository<com.lomeone.authentication.entity.Authentication, Long> {
    fun findByUid(uid: String): com.lomeone.authentication.entity.Authentication?

    fun findByEmailAndProvider(email: String, provider: com.lomeone.authentication.entity.AuthProvider): com.lomeone.authentication.entity.Authentication?

    fun findByEmailAndProviderAndRealm(email: String, provider: com.lomeone.authentication.entity.AuthProvider, realm: com.lomeone.authentication.entity.Realm): com.lomeone.authentication.entity.Authentication?

    fun findByEmailAndPassword(email: String, password: String): com.lomeone.authentication.entity.Authentication?

    fun findByUidAndRealm(uid: String, realm: com.lomeone.authentication.entity.Realm): com.lomeone.authentication.entity.Authentication?
}
