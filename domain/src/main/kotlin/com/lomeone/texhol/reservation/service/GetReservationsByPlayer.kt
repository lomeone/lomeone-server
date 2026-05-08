package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetReservationsByPlayer(
    private val playerRepository: PlayerRepository,
    private val reservationRepository: ReservationRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetReservationsByPlayerCommand): List<Reservation> {
        val player = playerRepository.findByIdOrNull(command.playerId)
            ?: throw PlayerNotFoundException(detail = mapOf("playerId" to command.playerId))

        return reservationRepository.findByPlayer(player)
    }
}

data class GetReservationsByPlayerCommand(
    val playerId: Long
)
