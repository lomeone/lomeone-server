package com.lomeone.texhol.reservation.repository

import com.lomeone.texhol.reservation.entity.Reservation

interface ReservationRepository {
    fun save(reservation: Reservation): Reservation
    fun findByIdAndSession(id: String, session: Int): Reservation?
    fun findByStoreBranchAndLatestGameType(storeBranch: String, gameType: String): Reservation?
    fun findByStoreBranchAndGameTypeAndSession(storeBranch: String, gameType: String, session: Int): Reservation?
}

