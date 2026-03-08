package com.lomeone.texhol.rest.api.reservation.dto

interface ReservationJoinDTO {
    data class Request(
        val storeBranch: String,
        val gameType: String,
        val joinUsers: List<String>,
        val reservationTime: String
    )
    data class Response(
        val gameType: String,
        val session: Int,
        val reservation: Map<String, String>
    )
}
