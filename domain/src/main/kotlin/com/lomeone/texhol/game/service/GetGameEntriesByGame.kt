package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetGameEntriesByGame(
    private val gameSessionRepository: GameSessionRepository,
    private val gameEntryRepository: GameEntryRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetGameEntriesByGameCommand): List<GameEntry> {
        logger.info { "Getting game entries: gameSessionId=${command.gameSessionId}" }
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))
        val entries = gameEntryRepository.findByGameSession(gameSession)
        logger.info { "Game entries found: count=${entries.size}" }
        return entries
    }
}

data class GetGameEntriesByGameCommand(
    val gameSessionId: Long
)
