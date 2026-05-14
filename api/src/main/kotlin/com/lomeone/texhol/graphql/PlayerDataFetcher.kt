package com.lomeone.texhol.graphql

import com.lomeone.generated.types.CreatePlayerInput
import com.lomeone.generated.types.CreatePlayerPayload
import com.lomeone.generated.types.FindOrCreatePlayerInput
import com.lomeone.generated.types.FindOrCreatePlayerPayload
import com.lomeone.generated.types.Player
import com.lomeone.texhol.player.service.CreatePlayer
import com.lomeone.texhol.player.service.CreatePlayerCommand
import com.lomeone.texhol.player.service.FindOrCreatePlayer
import com.lomeone.texhol.player.service.FindOrCreatePlayerCommand
import com.lomeone.texhol.player.service.GetPlayerByNickname
import com.lomeone.texhol.player.service.GetPlayerByNicknameCommand
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import com.netflix.graphql.dgs.DgsQuery
import com.netflix.graphql.dgs.InputArgument

@DgsComponent
class PlayerDataFetcher(
    private val createPlayer: CreatePlayer,
    private val getPlayerByNickname: GetPlayerByNickname,
    private val findOrCreatePlayer: FindOrCreatePlayer
) {
    @DgsQuery
    fun player(@InputArgument id: String): Player? {
        // 개별 조회는 추후 구현
        return null
    }

    @DgsQuery
    fun playerByNickname(@InputArgument nickname: String): Player? {
        return getPlayerByNickname(GetPlayerByNicknameCommand(nickname = nickname))?.toGraphQL()
    }

    @DgsMutation
    fun createPlayer(@InputArgument input: CreatePlayerInput): CreatePlayerPayload {
        val result = createPlayer(CreatePlayerCommand(nickname = input.nickname))
        // Payload는 추후 개선
        return CreatePlayerPayload(player = Player(
            id = result.id.toString(),
            nickname = input.nickname,
            createdAt = "",
            updatedAt = ""
        ))
    }

    @DgsMutation
    fun findOrCreatePlayer(@InputArgument input: FindOrCreatePlayerInput): FindOrCreatePlayerPayload {
        val result = findOrCreatePlayer(FindOrCreatePlayerCommand(nickname = input.nickname))
        return FindOrCreatePlayerPayload(player = result.toGraphQL())
    }

    private fun com.lomeone.texhol.player.entity.Player.toGraphQL() = Player(
        id = this.id.toString(),
        nickname = this.nickname,
        createdAt = this.createdAt.toString(),
        updatedAt = this.updatedAt.toString()
    )
}
