package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.player.service.FindOrCreatePlayer
import com.lomeone.texhol.player.service.FindOrCreatePlayerCommand
import com.lomeone.texhol.reservation.exception.ReservationAlreadyExistException
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class ChangeReservationPlayer(
    private val reservationRepository: ReservationRepository,
    private val findOrCreatePlayer: FindOrCreatePlayer
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: ChangeReservationPlayerCommand) {
        logger.info { "Changing reservation player: reservationId=${command.reservationId}, newNickname=${command.newNickname}" }
        val reservation = reservationRepository.findByIdOrNull(command.reservationId)
            ?: throw ReservationNotFoundException(detail = mapOf("reservationId" to command.reservationId))

        val player = findOrCreatePlayer(FindOrCreatePlayerCommand(nickname = command.newNickname))
        if (reservationRepository.existsByGameSessionAndPlayer(reservation.gameSession, player)) {
            throw ReservationAlreadyExistException(
                detail = mapOf("reservationId" to command.reservationId, "playerId" to player.id)
            )
        }
        reservation.changePlayer(player)
        logger.info { "Reservation player changed: reservationId=${command.reservationId}, playerId=${player.id}" }
    }
}

data class ChangeReservationPlayerCommand(
    val reservationId: Long,
    val newNickname: String
)
