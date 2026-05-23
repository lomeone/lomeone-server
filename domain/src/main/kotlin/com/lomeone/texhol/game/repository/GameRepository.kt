package com.lomeone.texhol.game.repository

import com.lomeone.texhol.game.entity.Game
import com.lomeone.texhol.store.entity.Store
import org.springframework.data.jpa.repository.JpaRepository

interface GameRepository : JpaRepository<Game, Long> {
    fun findByStore(store: Store): List<Game>
    fun findByStoreAndName(store: Store, name: String): Game?
    fun existsByStoreAndName(store: Store, name: String): Boolean
}
