package com.lomeone.texhol.game.repository

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.entity.GameSession
import org.springframework.data.jpa.repository.JpaRepository

interface GameEntryRepository : JpaRepository<GameEntry, Long> {
    fun findByGameSession(gameSession: GameSession): List<GameEntry>
    fun existsByGameSessionAndPlayer_Id(gameSession: GameSession, playerId: Long): Boolean
}
