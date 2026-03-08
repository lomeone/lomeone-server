package com.lomeone.texhol.rest.api.reservation.dto

interface ReservationLeaveDTO {
    data class Request(
        val storeBranch: String,
        val gameType: String,
        val leaveUsers: Set<String>
    )
    data class Response(
        val gameType: String,
        val session: Int,
        val reservation: Map<String, String>
    )
}
