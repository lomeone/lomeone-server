package com.lomeone.texhol.rest.api.reservation.dto

interface ReservationGetDTO {
    data class Request(
        val storeBranch: String,
        val gameType: String
    )
    data class Response(
        val gameType: String,
        val session: Int,
        val reservation: Map<String, String>,
        val status: String
    )
}
