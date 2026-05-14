package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSessionStatus
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdateGameSessionStatus(
    private val gameSessionRepository: GameSessionRepository
) {
    @Transactional
    operator fun invoke(command: UpdateGameSessionStatusCommand) {
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))

        when (command.status) {
            GameSessionStatus.RECRUITING -> gameSession.reOpen()
            GameSessionStatus.EARLY_CLOSE -> gameSession.earlyClose()
            GameSessionStatus.CLOSED -> gameSession.close()
        }
    }
}

data class UpdateGameSessionStatusCommand(
    val gameSessionId: Long,
    val status: GameSessionStatus
)
