package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.exception.ReservationClosedException
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service

@Service
class CloseReservationUseCase(
    private val reservationRepository: ReservationRepository
) {
    operator fun invoke(command: CloseReservationCommand): CloseReservationResult {
        val reservation = findReservation(command)

        ensureReservationOpened(reservation)

        reservation.closeReservation()

        val savedReservation = reservationRepository.save(reservation)

        return CloseReservationResult(
            storeBranch = savedReservation.storeBranch,
            gameType = savedReservation.gameType,
            session = savedReservation.session
        )
    }
    private fun findReservation(command: CloseReservationCommand): Reservation =
        reservationRepository.findByStoreBranchAndLatestGameType(command.storeBranch, command.gameType)
            ?: throw ReservationNotFoundException(
                detail = mapOf(
                    "storeBranch" to command.storeBranch,
                    "gameType" to command.gameType
                )
            )

    private fun ensureReservationOpened(reservation: Reservation) {
        reservation.isClosed() && throw ReservationClosedException(
            detail = mapOf(
                "storeBranch" to reservation.storeBranch,
                "gameType" to reservation.gameType,
                "session" to reservation.session
            )
        )
    }
}

data class CloseReservationCommand(
    val storeBranch: String,
    val gameType: String
)

data class CloseReservationResult(
    val storeBranch: String,
    val gameType: String,
    val session: Int
)
