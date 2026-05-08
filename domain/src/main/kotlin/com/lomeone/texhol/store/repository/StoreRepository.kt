package com.lomeone.texhol.store.repository

import com.lomeone.texhol.store.entity.Store
import org.springframework.data.jpa.repository.JpaRepository

interface StoreRepository : JpaRepository<Store, Long> {
    fun findByName(name: String): Store?
}
