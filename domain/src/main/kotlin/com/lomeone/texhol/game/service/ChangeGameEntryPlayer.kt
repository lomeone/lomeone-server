package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.exception.GameEntryAlreadyExistException
import com.lomeone.texhol.game.exception.GameEntryNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.player.service.FindOrCreatePlayer
import com.lomeone.texhol.player.service.FindOrCreatePlayerCommand
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ChangeGameEntryPlayer(
    private val gameEntryRepository: GameEntryRepository,
    private val findOrCreatePlayer: FindOrCreatePlayer
) {
    @Transactional
    operator fun invoke(command: ChangeGameEntryPlayerCommand) {
        val gameEntry = gameEntryRepository.findByIdOrNull(command.gameEntryId)
            ?: throw GameEntryNotFoundException(detail = mapOf("gameEntryId" to command.gameEntryId))

        val player = findOrCreatePlayer(FindOrCreatePlayerCommand(nickname = command.newNickname))
        if (gameEntryRepository.existsByGameSessionAndPlayer_Id(gameEntry.gameSession, player.id)) {
            throw GameEntryAlreadyExistException(
                detail = mapOf("gameEntryId" to command.gameEntryId, "playerId" to player.id)
            )
        }
        gameEntry.changePlayer(player)
    }
}

data class ChangeGameEntryPlayerCommand(
    val gameEntryId: Long,
    val newNickname: String
)
