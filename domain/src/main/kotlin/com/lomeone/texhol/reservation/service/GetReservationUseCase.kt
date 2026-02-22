package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.reservation.entity.ReservationStatus
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service

@Service
class GetReservationUseCase(
    private val reservationRepository: ReservationRepository
) {
    operator fun invoke(query: GetReservationQuery): GetReservationResult {
        val reservation = findReservation(query)

        return GetReservationResult(
            gameType = reservation.gameType,
            session = reservation.session,
            reservation = reservation.reservation,
            status = reservation.status
        )
    }

    private fun findReservation(query: GetReservationQuery) =
        reservationRepository.findByStoreBranchAndLatestGameType(query.storeBranch, query.gameType)
            ?: throw ReservationNotFoundException(
                detail = mapOf(
                    "storeBranch" to query.storeBranch,
                    "gameType" to query.gameType
                )
            )
}

data class GetReservationQuery(
    val storeBranch: String,
    val gameType: String
)

data class GetReservationResult(
    val gameType: String,
    val session: Int,
    val reservation: Map<String, String>,
    val status: ReservationStatus
)
