package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class CancelReservation(
    private val reservationRepository: ReservationRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: CancelReservationCommand) {
        logger.info { "Cancelling reservation: reservationId=${command.reservationId}" }
        val reservation = reservationRepository.findByIdOrNull(command.reservationId)
            ?: throw ReservationNotFoundException(detail = mapOf("reservationId" to command.reservationId))

        reservation.cancel()
        logger.info { "Reservation cancelled: reservationId=${command.reservationId}" }
    }
}

data class CancelReservationCommand(
    val reservationId: Long
)
