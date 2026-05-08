package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.player.service.FindOrCreatePlayer
import com.lomeone.texhol.player.service.FindOrCreatePlayerCommand
import com.lomeone.texhol.reservation.exception.ReservationAlreadyExistException
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChangeReservationPlayer(
    private val reservationRepository: ReservationRepository,
    private val findOrCreatePlayer: FindOrCreatePlayer
) {
    @Transactional
    operator fun invoke(command: ChangeReservationPlayerCommand) {
        val reservation = reservationRepository.findByIdOrNull(command.reservationId)
            ?: throw ReservationNotFoundException(detail = mapOf("reservationId" to command.reservationId))

        val player = findOrCreatePlayer(FindOrCreatePlayerCommand(nickname = command.newNickname))
        if (reservationRepository.existsByGameSessionAndPlayer(reservation.gameSession, player)) {
            throw ReservationAlreadyExistException(
                detail = mapOf("reservationId" to command.reservationId, "playerId" to player.id)
            )
        }
        reservation.changePlayer(player)
    }
}

data class ChangeReservationPlayerCommand(
    val reservationId: Long,
    val newNickname: String
)
