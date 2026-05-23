package com.lomeone.texhol.graphql

import com.lomeone.generated.types.CancelReservationInput
import com.lomeone.generated.types.CancelReservationPayload
import com.lomeone.generated.types.ChangeReservationPlayerInput
import com.lomeone.generated.types.ChangeReservationPlayerPayload
import com.lomeone.generated.types.CreateReservationInput
import com.lomeone.generated.types.CreateReservationPayload
import com.lomeone.generated.types.PaymentMethod
import com.lomeone.generated.types.RegisterReservationInput
import com.lomeone.generated.types.RegisterReservationPayload
import com.lomeone.generated.types.Reservation
import com.lomeone.generated.types.ReservationStatus
import com.lomeone.texhol.reservation.service.CancelReservation
import com.lomeone.texhol.reservation.service.CancelReservationCommand
import com.lomeone.texhol.reservation.service.ChangeReservationPlayer
import com.lomeone.texhol.reservation.service.ChangeReservationPlayerCommand
import com.lomeone.texhol.reservation.service.CreateReservation
import com.lomeone.texhol.reservation.service.CreateReservationCommand
import com.lomeone.texhol.reservation.service.GetReservationsByGameSessionSession
import com.lomeone.texhol.reservation.service.GetReservationsByGameSessionSessionCommand
import com.lomeone.texhol.reservation.service.GetReservationsByPlayer
import com.lomeone.texhol.reservation.service.GetReservationsByPlayerCommand
import com.lomeone.texhol.reservation.service.RegisterReservation
import com.lomeone.texhol.reservation.service.RegisterReservationCommand
import com.lomeone.texhol.player.service.GetPlayerByNickname
import com.lomeone.texhol.player.service.GetPlayerByNicknameCommand
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class ReservationDataFetcher(
    private val createReservation: CreateReservation,
    private val registerReservation: RegisterReservation,
    private val cancelReservation: CancelReservation,
    private val changeReservationPlayer: ChangeReservationPlayer,
    private val getReservationsByGameSession: GetReservationsByGameSessionSession,
    private val getReservationsByPlayer: GetReservationsByPlayer,
    private val getPlayerByNickname: GetPlayerByNickname
) {
    @DgsQuery
    fun reservationsByGameSession(@InputArgument gameSessionId: String): List<Reservation> {
        return getReservationsByGameSession(
            GetReservationsByGameSessionSessionCommand(gameSessionId = gameSessionId.toLong())
        ).map { it.toGraphQL() }
    }

    @DgsQuery
    fun reservationsByPlayer(@InputArgument playerId: String): List<Reservation> {
        return getReservationsByPlayer(
            GetReservationsByPlayerCommand(playerId = playerId.toLong())
        ).map { it.toGraphQL() }
    }

    @DgsMutation
    fun createReservation(@InputArgument input: CreateReservationInput): CreateReservationPayload {
        createReservation(
            CreateReservationCommand(
                gameSessionId = input.gameSessionId.toLong(),
                playerId = input.playerId.toLong(),
                reservationTime = input.reservationTime
            )
        )
        return CreateReservationPayload(reservation = createDummyReservation())
    }

    @DgsMutation
    fun registerReservation(@InputArgument input: RegisterReservationInput): RegisterReservationPayload {
        registerReservation(RegisterReservationCommand(
            reservationId = input.reservationId.toLong(),
            paymentMethod = input.paymentMethod.toEntity()
        ))
        return RegisterReservationPayload(reservation = createDummyReservation())
    }

    @DgsMutation
    fun cancelReservation(@InputArgument input: CancelReservationInput): CancelReservationPayload {
        cancelReservation(CancelReservationCommand(reservationId = input.reservationId.toLong()))
        return CancelReservationPayload(reservation = createDummyReservation())
    }

    @DgsMutation
    fun changeReservationPlayer(@InputArgument input: ChangeReservationPlayerInput): ChangeReservationPlayerPayload {
        val player = getPlayerByNickname(GetPlayerByNicknameCommand(nickname = "dummy"))
            ?: throw com.lomeone.texhol.player.exception.PlayerNotFoundException(detail = mapOf("playerId" to input.playerId))

        changeReservationPlayer(
            ChangeReservationPlayerCommand(
                reservationId = input.reservationId.toLong(),
                newNickname = player.nickname
            )
        )
        return ChangeReservationPlayerPayload(reservation = createDummyReservation())
    }

    private fun com.lomeone.texhol.reservation.entity.Reservation.toGraphQL() = Reservation(
        id = this.id.toString(),
        gameSession = this.gameSession.toGameSessionGraphQL(),
        player = this.player.toPlayerGraphQL(),
        time = this.time,
        status = this.status.toGraphQL(),
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )

    private fun com.lomeone.texhol.reservation.entity.ReservationStatus.toGraphQL() = when (this) {
        com.lomeone.texhol.reservation.entity.ReservationStatus.WAITING -> ReservationStatus.WAITING
        com.lomeone.texhol.reservation.entity.ReservationStatus.REGISTERED -> ReservationStatus.REGISTERED
        com.lomeone.texhol.reservation.entity.ReservationStatus.CANCELLED -> ReservationStatus.CANCELLED
    }

    private fun com.lomeone.texhol.game.entity.GameSession.toGameSessionGraphQL() =
        com.lomeone.generated.types.GameSession(
            id = this.id.toString(),
            store = this.store.toStoreGraphQL(),
            game = this.game.toGameGraphQL(),
            session = this.session,
            status = this.status.toGameSessionStatusGraphQL(),
            createdAt = this.createdAt.toString(),
            updatedAt = this.updatedAt.toString()
        )

    private fun com.lomeone.texhol.player.entity.Player.toPlayerGraphQL() = com.lomeone.generated.types.Player(
        id = this.id.toString(),
        nickname = this.nickname,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )

    private fun com.lomeone.texhol.store.entity.Store.toStoreGraphQL() = com.lomeone.generated.types.Store(
        id = this.id.toString(),
        name = this.name,
        location = this.location,
        address = this.address,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )

    private fun com.lomeone.texhol.game.entity.Game.toGameGraphQL() = com.lomeone.generated.types.Game(
        id = this.id.toString(),
        store = this.store.toStoreGraphQL(),
        name = this.name,
        scheduleType = this.scheduleType.toScheduleTypeGraphQL(),
        dayOfWeek = this.dayOfWeek,
        description = this.description,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )

    private fun com.lomeone.texhol.game.entity.ScheduleType.toScheduleTypeGraphQL() = when (this) {
        com.lomeone.texhol.game.entity.ScheduleType.DAILY -> com.lomeone.generated.types.ScheduleType.DAILY
        com.lomeone.texhol.game.entity.ScheduleType.WEEKLY -> com.lomeone.generated.types.ScheduleType.WEEKLY
        com.lomeone.texhol.game.entity.ScheduleType.MONTHLY -> com.lomeone.generated.types.ScheduleType.MONTHLY
        com.lomeone.texhol.game.entity.ScheduleType.CUSTOM -> com.lomeone.generated.types.ScheduleType.CUSTOM
    }

    private fun com.lomeone.texhol.game.entity.GameSessionStatus.toGameSessionStatusGraphQL() = when (this) {
        com.lomeone.texhol.game.entity.GameSessionStatus.RECRUITING -> com.lomeone.generated.types.GameSessionStatus.RECRUITING
        com.lomeone.texhol.game.entity.GameSessionStatus.EARLY_CLOSE -> com.lomeone.generated.types.GameSessionStatus.EARLY_CLOSE
        com.lomeone.texhol.game.entity.GameSessionStatus.CLOSED -> com.lomeone.generated.types.GameSessionStatus.CLOSED
    }

    private fun createDummyReservation() = Reservation(
        id = "0",
        gameSession = com.lomeone.generated.types.GameSession(
            id = "0",
            store = com.lomeone.generated.types.Store("0", "", "", null, "", "", ""),
            game = com.lomeone.generated.types.Game("0", com.lomeone.generated.types.Store("0", "", "", null, "", "", ""), "", com.lomeone.generated.types.ScheduleType.DAILY, null, null, "", ""),
            session = 0,
            status = com.lomeone.generated.types.GameSessionStatus.RECRUITING,
            createdAt = "",
            updatedAt = ""
        ),
        player = com.lomeone.generated.types.Player("0", "", "", ""),
        time = "",
        status = ReservationStatus.WAITING,
        createdAt = "",
        updatedAt = ""
    )

    private fun PaymentMethod.toEntity() = when (this) {
        PaymentMethod.CASH -> com.lomeone.texhol.game.entity.PaymentMethod.CASH
        PaymentMethod.CARD -> com.lomeone.texhol.game.entity.PaymentMethod.CARD
        PaymentMethod.POINTS -> com.lomeone.texhol.game.entity.PaymentMethod.POINTS
    }
}
