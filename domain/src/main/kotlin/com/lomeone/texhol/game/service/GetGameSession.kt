package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetGameSession(
    private val gameSessionRepository: GameSessionRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetGameSessionCommand): GameSession {
        return gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))
    }
}

data class GetGameSessionCommand(
    val gameSessionId: Long
)
