package com.lomeone.texhol.game.repository

import com.lomeone.texhol.game.entity.Game
import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.GameSessionStatus
import com.lomeone.texhol.store.entity.Store
import org.springframework.data.jpa.repository.JpaRepository

interface GameSessionRepository : JpaRepository<GameSession, Long> {
    fun findByStoreAndStatus(store: Store, status: GameSessionStatus): List<GameSession>
    fun findByStore(store: Store): List<GameSession>
    fun findByGame(game: Game): List<GameSession>
    fun existsByStoreAndGameAndSession(store: Store, game: Game, session: Int): Boolean
}
