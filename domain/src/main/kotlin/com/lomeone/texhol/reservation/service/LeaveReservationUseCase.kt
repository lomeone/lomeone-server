package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.exception.ReservationClosedException
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service

@Service
class LeaveReservationUseCase(
    private val reservationRepository: ReservationRepository
) {
    operator fun invoke(command: LeaveReservationCommand): LeaveReservationResult {
        val reservation = findReservation(command)

        ensureReservationOpened(reservation)

        command.cancelUsers.forEach {
            reservation.cancel(it)
        }

        val savedReservation = reservationRepository.save(reservation)

        return LeaveReservationResult(
            gameType = savedReservation.gameType,
            session = savedReservation.session,
            reservation = savedReservation.reservation
        )
    }

    private fun findReservation(command: LeaveReservationCommand): Reservation =
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

data class LeaveReservationCommand(
    val storeBranch: String,
    val gameType: String,
    val cancelUsers: Set<String>
)

data class LeaveReservationResult(
    val gameType: String,
    val session: Int,
    val reservation: Map<String, String>
)
