package com.lomeone.texhol.rest.api.reservation

import com.lomeone.texhol.reservation.service.CloseReservationCommand
import com.lomeone.texhol.reservation.service.LeaveReservationUseCase
import com.lomeone.texhol.reservation.service.CloseReservationUseCase
import com.lomeone.texhol.reservation.service.GetReservationQuery
import com.lomeone.texhol.reservation.service.GetReservationUseCase
import com.lomeone.texhol.reservation.service.JoinReservationCommand
import com.lomeone.texhol.reservation.service.JoinReservationUseCase
import com.lomeone.texhol.reservation.service.LeaveReservationCommand
import com.lomeone.texhol.reservation.service.StartReservationCommand
import com.lomeone.texhol.reservation.service.StartReservationUseCase
import com.lomeone.texhol.rest.api.reservation.dto.ReservationCloseDTO
import com.lomeone.texhol.rest.api.reservation.dto.ReservationGetDTO
import com.lomeone.texhol.rest.api.reservation.dto.ReservationJoinDTO
import com.lomeone.texhol.rest.api.reservation.dto.ReservationLeaveDTO
import com.lomeone.texhol.rest.api.reservation.dto.ReservationStartDTO
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/texhol/api/v1/reservations")
class ReservationController(
    private val getReservation: GetReservationUseCase,
    private val startReservation: StartReservationUseCase,
    private val closeReservation: CloseReservationUseCase,
    private val joinReservation: JoinReservationUseCase,
    private val leaveReservation: LeaveReservationUseCase
) {
    @GetMapping
    fun get(request: ReservationGetDTO.Request): ReservationGetDTO.Response {
        val result = getReservation(GetReservationQuery(request.storeBranch, request.gameType))
        return ReservationGetDTO.Response(
            gameType = result.gameType,
            session = result.session,
            reservation = result.reservation,
            status = result.status.name
        )
    }

    @PostMapping("/start")
    fun start(@RequestBody request: ReservationStartDTO.Request): ReservationStartDTO.Response {
        val result = startReservation(StartReservationCommand(
            storeBranch = request.storeBranch,
            gameType = request.gameType,
            session = request.session
        ))

        return ReservationStartDTO.Response(
            storeBranch = result.storeBranch,
            gameType = result.gameType,
            session = result.session,
            reservation = result.reservation
        )
    }

    @PostMapping("/close")
    fun close(@RequestBody request: ReservationCloseDTO.Request): ReservationCloseDTO.Response {
        val result = closeReservation(CloseReservationCommand(request.storeBranch, request.gameType))
        return ReservationCloseDTO.Response(result.storeBranch, result.gameType, result.session)
    }

    @PostMapping("/join")
    fun join(@RequestBody request: ReservationJoinDTO.Request): ReservationJoinDTO.Response {
        val result = joinReservation(JoinReservationCommand(
            storeBranch = request.storeBranch,
            gameType = request.gameType,
            joinUsers = request.joinUsers.toSet(),
            reservationTime = request.reservationTime
        ))
        return ReservationJoinDTO.Response(result.gameType, result.session, result.reservation)
    }

    @PostMapping("/leave")
    fun leave(@RequestBody request: ReservationLeaveDTO.Request): ReservationLeaveDTO.Response {
        val result = leaveReservation(LeaveReservationCommand(
            storeBranch = request.storeBranch,
            gameType = request.gameType,
            leaveUsers = request.leaveUsers
        ))
        return ReservationLeaveDTO.Response(result.gameType, result.session, result.reservation)
    }
}
