package com.lomeone.domain.authentication.repository

import com.lomeone.domain.authentication.entity.Realm
import org.springframework.data.jpa.repository.JpaRepository

interface RealmRepository : JpaRepository<Realm, Long> {
    fun findByCode(code: String): Realm?
}
