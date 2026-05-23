package com.lomeone.texhol.graphql

import com.lomeone.generated.types.CreateGameInput
import com.lomeone.generated.types.CreateGamePayload
import com.lomeone.generated.types.Game
import com.lomeone.generated.types.ScheduleType
import com.lomeone.generated.types.Store
import com.lomeone.texhol.game.service.CreateGame
import com.lomeone.texhol.game.service.CreateGameCommand
import com.lomeone.texhol.game.service.GetGamesByStore
import com.lomeone.texhol.game.service.GetGamesByStoreCommand
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class GameDataFetcher(
    private val createGame: CreateGame,
    private val getGamesByStore: GetGamesByStore
) {
    @DgsQuery
    fun game(@InputArgument id: String): Game? {
        return null
    }

    @DgsQuery
    fun gamesByStore(@InputArgument storeId: String): List<Game> {
        return getGamesByStore(GetGamesByStoreCommand(storeId = storeId.toLong()))
            .map { it.toGraphQL() }
    }

    @DgsMutation
    fun createGame(@InputArgument input: CreateGameInput): CreateGamePayload {
        val result = createGame(
            CreateGameCommand(
                storeId = input.storeId.toLong(),
                name = input.name,
                scheduleType = input.scheduleType.toEntity(),
                dayOfWeek = input.dayOfWeek,
                description = input.description
            )
        )
        return CreateGamePayload(game = Game(
            id = result.id.toString(),
            store = Store("0", "", "", null, "", "", ""),
            name = input.name,
            scheduleType = input.scheduleType,
            dayOfWeek = input.dayOfWeek,
            description = input.description,
            createdAt = "",
            updatedAt = ""
        ))
    }

    private fun com.lomeone.texhol.game.entity.Game.toGraphQL() = Game(
        id = this.id.toString(),
        store = this.store.toGraphQL(),
        name = this.name,
        scheduleType = this.scheduleType.toGraphQL(),
        dayOfWeek = this.dayOfWeek,
        description = this.description,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )

    private fun com.lomeone.texhol.game.entity.ScheduleType.toGraphQL() = when (this) {
        com.lomeone.texhol.game.entity.ScheduleType.DAILY -> ScheduleType.DAILY
        com.lomeone.texhol.game.entity.ScheduleType.WEEKLY -> ScheduleType.WEEKLY
        com.lomeone.texhol.game.entity.ScheduleType.MONTHLY -> ScheduleType.MONTHLY
        com.lomeone.texhol.game.entity.ScheduleType.CUSTOM -> ScheduleType.CUSTOM
    }

    private fun ScheduleType.toEntity() = when (this) {
        ScheduleType.DAILY -> com.lomeone.texhol.game.entity.ScheduleType.DAILY
        ScheduleType.WEEKLY -> com.lomeone.texhol.game.entity.ScheduleType.WEEKLY
        ScheduleType.MONTHLY -> com.lomeone.texhol.game.entity.ScheduleType.MONTHLY
        ScheduleType.CUSTOM -> com.lomeone.texhol.game.entity.ScheduleType.CUSTOM
    }

    private fun com.lomeone.texhol.store.entity.Store.toGraphQL() = Store(
        id = this.id.toString(),
        name = this.name,
        location = this.location,
        address = this.address,
        imageUrl = this.imageUrl,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}
