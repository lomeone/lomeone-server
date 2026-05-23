package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSessionStatus
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class UpdateGameSessionStatus(
    private val gameSessionRepository: GameSessionRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: UpdateGameSessionStatusCommand) {
        logger.info { "Updating game session status: gameSessionId=${command.gameSessionId}, status=${command.status}" }
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))

        when (command.status) {
            GameSessionStatus.RECRUITING -> gameSession.reOpen()
            GameSessionStatus.EARLY_CLOSE -> gameSession.earlyClose()
            GameSessionStatus.CLOSED -> gameSession.close()
        }
        logger.info { "Game session status updated: gameSessionId=${command.gameSessionId}" }
    }
}

data class UpdateGameSessionStatusCommand(
    val gameSessionId: Long,
    val status: GameSessionStatus
)
