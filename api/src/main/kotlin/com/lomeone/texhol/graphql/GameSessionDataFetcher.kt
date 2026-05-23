package com.lomeone.texhol.graphql

import com.lomeone.generated.types.CreateGameSessionInput
import com.lomeone.generated.types.CreateGameSessionPayload
import com.lomeone.generated.types.GameSession
import com.lomeone.generated.types.GameSessionStatus
import com.lomeone.generated.types.UpdateGameSessionStatusInput
import com.lomeone.generated.types.UpdateGameSessionStatusPayload
import com.lomeone.texhol.game.service.CreateGameSession
import com.lomeone.texhol.game.service.CreateGameSessionCommand
import com.lomeone.texhol.game.service.GetGameSession
import com.lomeone.texhol.game.service.GetGameSessionCommand
import com.lomeone.texhol.game.service.GetGameSessionsByStore
import com.lomeone.texhol.game.service.GetGameSessionsByStoreCommand
import com.lomeone.texhol.game.service.UpdateGameSessionStatus
import com.lomeone.texhol.game.service.UpdateGameSessionStatusCommand
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class GameSessionDataFetcher(
    private val createGameSession: CreateGameSession,
    private val getGameSession: GetGameSession,
    private val getGameSessionsByStore: GetGameSessionsByStore,
    private val updateGameSessionStatus: UpdateGameSessionStatus
) {
    @DgsQuery
    fun gameSession(@InputArgument id: String): GameSession? {
        val gameSessionEntity = getGameSession(GetGameSessionCommand(gameSessionId = id.toLong()))
        return gameSessionEntity.toGraphQL()
    }

    @DgsQuery
    fun gameSessionsByStore(
        @InputArgument storeId: String,
        @InputArgument status: GameSessionStatus?
    ): List<GameSession> {
        return getGameSessionsByStore(
            GetGameSessionsByStoreCommand(
                storeId = storeId.toLong(),
                status = status?.toEntity()
            )
        ).map { it.toGraphQL() }
    }

    @DgsMutation
    fun createGameSession(@InputArgument input: CreateGameSessionInput): CreateGameSessionPayload {
        val result = createGameSession(
            CreateGameSessionCommand(
                storeId = input.storeId.toLong(),
                gameId = input.gameId.toLong(),
                session = input.session
            )
        )
        val gameSessionEntity = getGameSession(GetGameSessionCommand(gameSessionId = result.id))
        return CreateGameSessionPayload(gameSession = gameSessionEntity.toGraphQL())
    }

    @DgsMutation
    fun updateGameSessionStatus(@InputArgument input: UpdateGameSessionStatusInput): UpdateGameSessionStatusPayload {
        updateGameSessionStatus(
            UpdateGameSessionStatusCommand(
                gameSessionId = input.gameSessionId.toLong(),
                status = input.status.toEntity()
            )
        )
        val gameSessionEntity = getGameSession(GetGameSessionCommand(gameSessionId = input.gameSessionId.toLong()))
        return UpdateGameSessionStatusPayload(gameSession = gameSessionEntity.toGraphQL())
    }

    private fun com.lomeone.texhol.game.entity.GameSession.toGraphQL() = GameSession(
        id = this.id.toString(),
        store = this.store.toStoreGraphQL(),
        game = this.game.toGameGraphQL(),
        session = this.session,
        status = this.status.toGraphQL(),
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )

    private fun com.lomeone.texhol.game.entity.GameSessionStatus.toGraphQL() = when (this) {
        com.lomeone.texhol.game.entity.GameSessionStatus.RECRUITING -> GameSessionStatus.RECRUITING
        com.lomeone.texhol.game.entity.GameSessionStatus.EARLY_CLOSE -> GameSessionStatus.EARLY_CLOSE
        com.lomeone.texhol.game.entity.GameSessionStatus.CLOSED -> GameSessionStatus.CLOSED
    }

    private fun GameSessionStatus.toEntity() = when (this) {
        GameSessionStatus.RECRUITING -> com.lomeone.texhol.game.entity.GameSessionStatus.RECRUITING
        GameSessionStatus.EARLY_CLOSE -> com.lomeone.texhol.game.entity.GameSessionStatus.EARLY_CLOSE
        GameSessionStatus.CLOSED -> com.lomeone.texhol.game.entity.GameSessionStatus.CLOSED
    }

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
}
