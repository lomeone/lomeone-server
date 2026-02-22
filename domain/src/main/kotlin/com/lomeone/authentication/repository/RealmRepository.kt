package com.lomeone.authentication.repository

import com.lomeone.authentication.entity.Realm
import org.springframework.data.jpa.repository.JpaRepository

interface RealmRepository : JpaRepository<com.lomeone.authentication.entity.Realm, Long> {
    fun findByCode(code: String): com.lomeone.authentication.entity.Realm?
}
