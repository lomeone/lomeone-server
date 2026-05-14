package com.lomeone.texhol.reservation.repository

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.reservation.entity.Reservation
import org.springframework.data.jpa.repository.JpaRepository

interface ReservationRepository : JpaRepository<Reservation, Long> {
    fun findByGameSession(gameSession: GameSession): List<Reservation>
    fun findByPlayer(player: Player): List<Reservation>
    fun existsByGameSessionAndPlayer(gameSession: GameSession, player: Player): Boolean
}
