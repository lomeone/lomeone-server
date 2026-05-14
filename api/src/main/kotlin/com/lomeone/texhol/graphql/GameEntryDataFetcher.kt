package com.lomeone.texhol.graphql

import com.lomeone.generated.types.AddReBuyInput
import com.lomeone.generated.types.AddReBuyPayload
import com.lomeone.generated.types.BuyInRecord
import com.lomeone.generated.types.BuyInType
import com.lomeone.generated.types.ChangeGameEntryPlayerInput
import com.lomeone.generated.types.ChangeGameEntryPlayerPayload
import com.lomeone.generated.types.CreateGameEntryInput
import com.lomeone.generated.types.CreateGameEntryPayload
import com.lomeone.generated.types.GameEntry
import com.lomeone.generated.types.GameEntryStatus
import com.lomeone.generated.types.PaymentMethod
import com.lomeone.generated.types.ToggleSitOutInput
import com.lomeone.generated.types.ToggleSitOutPayload
import com.lomeone.texhol.game.service.AddReBuy
import com.lomeone.texhol.game.service.AddReBuyCommand
import com.lomeone.texhol.game.service.ChangeGameEntryPlayer
import com.lomeone.texhol.game.service.ChangeGameEntryPlayerCommand
import com.lomeone.texhol.game.service.CreateGameEntry
import com.lomeone.texhol.game.service.CreateGameEntryCommand
import com.lomeone.texhol.game.service.GetGameEntriesByGame
import com.lomeone.texhol.game.service.GetGameEntriesByGameCommand
import com.lomeone.texhol.game.service.ToggleSitOut
import com.lomeone.texhol.game.service.ToggleSitOutCommand
import com.lomeone.texhol.player.service.GetPlayerByNickname
import com.lomeone.texhol.player.service.GetPlayerByNicknameCommand
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class GameEntryDataFetcher(
    private val createGameEntry: CreateGameEntry,
    private val addReBuy: AddReBuy,
    private val toggleSitOut: ToggleSitOut,
    private val changeGameEntryPlayer: ChangeGameEntryPlayer,
    private val getGameEntriesByGame: GetGameEntriesByGame,
    private val getPlayerByNickname: GetPlayerByNickname
) {
    @DgsQuery
    fun gameEntry(@InputArgument id: String): GameEntry? {
        // 개별 조회는 추후 구현
        return null
    }

    @DgsQuery
    fun gameEntriesByGameSession(@InputArgument gameSessionId: String): List<GameEntry> {
        return getGameEntriesByGame(
            GetGameEntriesByGameCommand(gameSessionId = gameSessionId.toLong())
        ).map { it.toGraphQL() }
    }

    @DgsMutation
    fun createGameEntry(@InputArgument input: CreateGameEntryInput): CreateGameEntryPayload {
        val result = createGameEntry(
            CreateGameEntryCommand(
                gameSessionId = input.gameSessionId.toLong(),
                playerId = input.playerId.toLong(),
                paymentMethod = input.paymentMethod.toEntity()
            )
        )
        // Payload 구성은 추후 개선
        return CreateGameEntryPayload(gameEntry = GameEntry(
            id = result.id.toString(),
            gameSession = createDummyGameSession(),
            player = createDummyPlayer(),
            status = GameEntryStatus.ALIVE,
            buyInRecords = emptyList(),
            reBuyCount = 0,
            createdAt = "",
            updatedAt = ""
        ))
    }

    @DgsMutation
    fun addReBuy(@InputArgument input: AddReBuyInput): AddReBuyPayload {
        addReBuy(
            AddReBuyCommand(
                gameEntryId = input.gameEntryId.toLong(),
                paymentMethod = input.paymentMethod.toEntity()
            )
        )
        return AddReBuyPayload(gameEntry = GameEntry(
            id = input.gameEntryId,
            gameSession = createDummyGameSession(),
            player = createDummyPlayer(),
            status = GameEntryStatus.ALIVE,
            buyInRecords = emptyList(),
            reBuyCount = 0,
            createdAt = "",
            updatedAt = ""
        ))
    }

    @DgsMutation
    fun toggleSitOut(@InputArgument input: ToggleSitOutInput): ToggleSitOutPayload {
        toggleSitOut(ToggleSitOutCommand(gameEntryId = input.gameEntryId.toLong()))
        return ToggleSitOutPayload(gameEntry = GameEntry(
            id = input.gameEntryId,
            gameSession = createDummyGameSession(),
            player = createDummyPlayer(),
            status = GameEntryStatus.SIT_OUT,
            buyInRecords = emptyList(),
            reBuyCount = 0,
            createdAt = "",
            updatedAt = ""
        ))
    }

    @DgsMutation
    fun changeGameEntryPlayer(@InputArgument input: ChangeGameEntryPlayerInput): ChangeGameEntryPlayerPayload {
        val player = getPlayerByNickname(GetPlayerByNicknameCommand(nickname = "dummy"))
            ?: throw com.lomeone.texhol.player.exception.PlayerNotFoundException(detail = mapOf("playerId" to input.playerId))

        changeGameEntryPlayer(
            ChangeGameEntryPlayerCommand(
                gameEntryId = input.gameEntryId.toLong(),
                newNickname = player.nickname
            )
        )
        return ChangeGameEntryPlayerPayload(gameEntry = GameEntry(
            id = input.gameEntryId,
            gameSession = createDummyGameSession(),
            player = createDummyPlayer(),
            status = GameEntryStatus.ALIVE,
            buyInRecords = emptyList(),
            reBuyCount = 0,
            createdAt = "",
            updatedAt = ""
        ))
    }

    private fun com.lomeone.texhol.game.entity.GameEntry.toGraphQL() = GameEntry(
        id = this.id.toString(),
        gameSession = this.gameSession.toGameSessionGraphQL(),
        player = this.player.toPlayerGraphQL(),
        status = this.status.toGraphQL(),
        buyInRecords = this.buyInRecords.map { it.toGraphQL() },
        reBuyCount = this.reBuyCount,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )

    private fun com.lomeone.texhol.game.entity.GameEntryStatus.toGraphQL() = when (this) {
        com.lomeone.texhol.game.entity.GameEntryStatus.ALIVE -> GameEntryStatus.ALIVE
        com.lomeone.texhol.game.entity.GameEntryStatus.SIT_OUT -> GameEntryStatus.SIT_OUT
    }

    private fun com.lomeone.texhol.game.entity.BuyInRecord.toGraphQL() = BuyInRecord(
        type = this.type.toGraphQL(),
        paymentMethod = this.paymentMethod.toGraphQL(),
        createdAt = this.createdAt.toString()
    )

    private fun com.lomeone.texhol.game.entity.BuyInType.toGraphQL() = when (this) {
        com.lomeone.texhol.game.entity.BuyInType.INITIAL -> BuyInType.INITIAL
        com.lomeone.texhol.game.entity.BuyInType.RE_BUY -> BuyInType.RE_BUY
    }

    private fun com.lomeone.texhol.game.entity.PaymentMethod.toGraphQL() = when (this) {
        com.lomeone.texhol.game.entity.PaymentMethod.CASH -> PaymentMethod.CASH
        com.lomeone.texhol.game.entity.PaymentMethod.CARD -> PaymentMethod.CARD
        com.lomeone.texhol.game.entity.PaymentMethod.POINTS -> PaymentMethod.POINTS
    }

    private fun PaymentMethod.toEntity() = when (this) {
        PaymentMethod.CASH -> com.lomeone.texhol.game.entity.PaymentMethod.CASH
        PaymentMethod.CARD -> com.lomeone.texhol.game.entity.PaymentMethod.CARD
        PaymentMethod.POINTS -> com.lomeone.texhol.game.entity.PaymentMethod.POINTS
    }

    private fun com.lomeone.texhol.game.entity.GameSession.toGameSessionGraphQL() =
        com.lomeone.generated.types.GameSession(
            id = this.id.toString(),
            store = this.store.toStoreGraphQL(),
            gameType = this.gameType.toGameTypeGraphQL(),
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

    private fun com.lomeone.texhol.game.entity.GameType.toGameTypeGraphQL() = com.lomeone.generated.types.GameType(
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

    private fun createDummyGameSession() = com.lomeone.generated.types.GameSession(
        id = "0",
        store = createDummyStore(),
        gameType = createDummyGameType(),
        session = 0,
        status = com.lomeone.generated.types.GameSessionStatus.RECRUITING,
        createdAt = "",
        updatedAt = ""
    )

    private fun createDummyPlayer() = com.lomeone.generated.types.Player("0", "", "", "")

    private fun createDummyStore() = com.lomeone.generated.types.Store("0", "", "", null, "", "", "")

    private fun createDummyGameType() = com.lomeone.generated.types.GameType(
        "0",
        createDummyStore(),
        "",
        com.lomeone.generated.types.ScheduleType.DAILY,
        null,
        null,
        "",
        ""
    )
}
