package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateGameEntry(
    private val gameSessionRepository: GameSessionRepository,
    private val playerRepository: PlayerRepository,
    private val gameEntryRepository: GameEntryRepository
) {
    @Transactional
    operator fun invoke(command: CreateGameEntryCommand): CreateGameEntryResult {
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))

        val player = playerRepository.findByIdOrNull(command.playerId)
            ?: throw PlayerNotFoundException(detail = mapOf("playerId" to command.playerId))

        val gameEntry = GameEntry.create(
            gameSession = gameSession,
            player = player,
            initialPayment = command.paymentMethod
        )
        val savedGameEntry = gameEntryRepository.save(gameEntry)

        return CreateGameEntryResult(id = savedGameEntry.id)
    }
}

data class CreateGameEntryCommand(
    val gameSessionId: Long,
    val playerId: Long,
    val paymentMethod: PaymentMethod
)

data class CreateGameEntryResult(
    val id: Long
)
