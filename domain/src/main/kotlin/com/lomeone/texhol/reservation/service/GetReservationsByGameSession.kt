package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetReservationsByGameSessionSession(
    private val gameSessionRepository: GameSessionRepository,
    private val reservationRepository: ReservationRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetReservationsByGameSessionSessionCommand): List<Reservation> {
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))

        return reservationRepository.findByGameSession(gameSession)
    }
}

data class GetReservationsByGameSessionSessionCommand(
    val gameSessionId: Long
)
