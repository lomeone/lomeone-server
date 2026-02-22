package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.exception.ReservationClosedException
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service

@Service
class JoinReservationUseCase(
    private val reservationRepository: ReservationRepository
) {
    operator fun invoke(command: JoinReservationCommand): JoinReservationResult {
        val reservation = findReservation(command)

        ensureReservationOpen(reservation)

        command.reservationUsers.forEach {
            reservation.reserve(it, command.reservationTime)
        }

        val savedReservation = reservationRepository.save(reservation)

        return JoinReservationResult(
            gameType = savedReservation.gameType,
            session = savedReservation.session,
            reservation = savedReservation.reservation
        )
    }

    private fun findReservation(command: JoinReservationCommand): Reservation =
        reservationRepository.findByStoreBranchAndLatestGameType(command.storeBranch, command.gameType)
            ?: throw ReservationNotFoundException(
                detail = mapOf(
                    "storeBranch" to command.storeBranch,
                    "gameType" to command.gameType
                )
            )

    private fun ensureReservationOpen(reservation: Reservation) {
        reservation.isClosed() && throw ReservationClosedException(
            detail = mapOf(
                "storeBranch" to reservation.storeBranch,
                "gameType" to reservation.gameType,
                "session" to reservation.session
            )
        )
    }
}

data class JoinReservationCommand(
    val storeBranch: String,
    val gameType: String,
    val reservationUsers: Set<String>,
    val reservationTime: String
)

data class JoinReservationResult(
    val gameType: String,
    val session: Int,
    val reservation: Map<String, String>
)
