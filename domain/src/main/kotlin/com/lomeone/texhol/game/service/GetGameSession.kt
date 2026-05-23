package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetGameSession(
    private val gameSessionRepository: GameSessionRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetGameSessionCommand): GameSession {
        logger.info { "Getting game session: gameSessionId=${command.gameSessionId}" }
        return gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))
    }
}

data class GetGameSessionCommand(
    val gameSessionId: Long
)
