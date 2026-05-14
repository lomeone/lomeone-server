package com.lomeone.texhol.game.repository

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.GameSessionStatus
import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.store.entity.Store
import org.springframework.data.jpa.repository.JpaRepository

interface GameSessionRepository : JpaRepository<GameSession, Long> {
    fun findByStoreAndStatus(store: Store, status: GameSessionStatus): List<GameSession>
    fun findByStore(store: Store): List<GameSession>
    fun findByGameType(gameType: GameType): List<GameSession>
    fun existsByStoreAndGameTypeAndSession(store: Store, gameType: GameType, session: Int): Boolean
}
