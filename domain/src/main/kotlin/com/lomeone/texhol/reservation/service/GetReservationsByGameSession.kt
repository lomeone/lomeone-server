package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.repository.ReservationRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetReservationsByGameSessionSession(
    private val gameSessionRepository: GameSessionRepository,
    private val reservationRepository: ReservationRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetReservationsByGameSessionSessionCommand): List<Reservation> {
        logger.info { "Getting reservations: gameSessionId=${command.gameSessionId}" }
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))

        val reservations = reservationRepository.findByGameSession(gameSession)
        logger.info { "Reservations found: count=${reservations.size}" }
        return reservations
    }
}

data class GetReservationsByGameSessionSessionCommand(
    val gameSessionId: Long
)
