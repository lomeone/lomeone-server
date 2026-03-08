package com.lomeone.texhol.rest.api.reservation.dto

interface ReservationStartDTO {
    data class Request(
        val storeBranch: String,
        val gameType: String,
        val session: Int? = null
    )
    data class Response(
        val storeBranch: String,
        val gameType: String,
        val session: Int,
        val reservation: Map<String, String>
    )
}
