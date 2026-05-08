package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CancelReservation(
    private val reservationRepository: ReservationRepository
) {
    @Transactional
    operator fun invoke(command: CancelReservationCommand) {
        val reservation = reservationRepository.findByIdOrNull(command.reservationId)
            ?: throw ReservationNotFoundException(detail = mapOf("reservationId" to command.reservationId))

        reservation.cancel()
    }
}

data class CancelReservationCommand(
    val reservationId: Long
)
