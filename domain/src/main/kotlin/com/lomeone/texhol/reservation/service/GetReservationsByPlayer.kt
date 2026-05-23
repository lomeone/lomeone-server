package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.repository.ReservationRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetReservationsByPlayer(
    private val playerRepository: PlayerRepository,
    private val reservationRepository: ReservationRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetReservationsByPlayerCommand): List<Reservation> {
        logger.info { "Getting reservations: playerId=${command.playerId}" }
        val player = playerRepository.findByIdOrNull(command.playerId)
            ?: throw PlayerNotFoundException(detail = mapOf("playerId" to command.playerId))

        val reservations = reservationRepository.findByPlayer(player)
        logger.info { "Reservations found: count=${reservations.size}" }
        return reservations
    }
}

data class GetReservationsByPlayerCommand(
    val playerId: Long
)
