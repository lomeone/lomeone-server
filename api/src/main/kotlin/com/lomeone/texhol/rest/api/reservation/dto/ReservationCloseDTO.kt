package com.lomeone.texhol.rest.api.reservation.dto

interface ReservationCloseDTO {
    data class Request(
        val storeBranch: String,
        val gameType: String
    )
    data class Response(
        val storeBranch: String,
        val gameType: String,
        val session: Int
    )
}
