package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetGameEntriesByGame(
    private val gameSessionRepository: GameSessionRepository,
    private val gameEntryRepository: GameEntryRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetGameEntriesByGameCommand): List<GameEntry> {
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))
        return gameEntryRepository.findByGameSession(gameSession)
    }
}

data class GetGameEntriesByGameCommand(
    val gameSessionId: Long
)
