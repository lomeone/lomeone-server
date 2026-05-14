package com.lomeone.texhol.game.repository

import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.store.entity.Store
import org.springframework.data.jpa.repository.JpaRepository

interface GameTypeRepository : JpaRepository<GameType, Long> {
    fun findByStore(store: Store): List<GameType>
    fun findByStoreAndName(store: Store, name: String): GameType?
    fun existsByStoreAndName(store: Store, name: String): Boolean
}
