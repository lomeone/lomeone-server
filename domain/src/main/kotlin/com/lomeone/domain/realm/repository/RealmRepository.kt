package com.lomeone.domain.realm.repository

import com.lomeone.domain.realm.entity.Realm
import org.springframework.data.jpa.repository.JpaRepository

interface RealmRepository : JpaRepository<Realm, Long> {
    fun findByCode(code: String): Realm?
}
