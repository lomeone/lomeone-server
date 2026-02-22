package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.stereotype.Service

@Service
class ReserveService(
    private val reservationRepository: ReservationRepository,
) {
}
