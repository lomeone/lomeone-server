package com.lomeone.texhol.graphql

import com.lomeone.generated.types.CreateGameTypeInput
import com.lomeone.generated.types.CreateGameTypePayload
import com.lomeone.generated.types.GameType
import com.lomeone.generated.types.ScheduleType
import com.lomeone.generated.types.Store
import com.lomeone.texhol.game.service.CreateGameType
import com.lomeone.texhol.game.service.CreateGameTypeCommand
import com.lomeone.texhol.game.service.GetGameTypesByStore
import com.lomeone.texhol.game.service.GetGameTypesByStoreCommand
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class GameTypeDataFetcher(
    private val createGameType: CreateGameType,
    private val getGameTypesByStore: GetGameTypesByStore
) {
    @DgsQuery
    fun gameType(@InputArgument id: String): GameType? {
        // 개별 조회는 추후 구현
        return null
    }

    @DgsQuery
    fun gameTypesByStore(@InputArgument storeId: String): List<GameType> {
        return getGameTypesByStore(GetGameTypesByStoreCommand(storeId = storeId.toLong()))
            .map { it.toGraphQL() }
    }

    @DgsMutation
    fun createGameType(@InputArgument input: CreateGameTypeInput): CreateGameTypePayload {
        val result = createGameType(
            CreateGameTypeCommand(
                storeId = input.storeId.toLong(),
                name = input.name,
                scheduleType = input.scheduleType.toEntity(),
                dayOfWeek = input.dayOfWeek,
                description = input.description
            )
        )
        // Payload는 추후 개선
        return CreateGameTypePayload(gameType = GameType(
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

    private fun com.lomeone.texhol.game.entity.GameType.toGraphQL() = GameType(
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
