package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateReservation(
    private val reservationRepository: ReservationRepository,
    private val gameSessionRepository: GameSessionRepository,
    private val playerRepository: PlayerRepository
) {
    @Transactional
    operator fun invoke(command: CreateReservationCommand): CreateReservationResult {
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))

        val player = playerRepository.findByIdOrNull(command.playerId)
            ?: throw PlayerNotFoundException(detail = mapOf("playerId" to command.playerId))

        val reservation = Reservation(gameSession = gameSession, player = player, time = command.reservationTime)
        val savedReservation = reservationRepository.save(reservation)

        return CreateReservationResult(id = savedReservation.id)
    }
}

data class CreateReservationCommand(
    val gameSessionId: Long,
    val playerId: Long,
    val reservationTime: String
)

data class CreateReservationResult(
    val id: Long
)

