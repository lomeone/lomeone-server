package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntryStatus
import com.lomeone.texhol.game.exception.GameEntryNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class ToggleSitOut(
    private val gameEntryRepository: GameEntryRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: ToggleSitOutCommand) {
        logger.info { "Toggling sit-out: gameEntryId=${command.gameEntryId}" }
        val gameEntry = gameEntryRepository.findByIdOrNull(command.gameEntryId)
            ?: throw GameEntryNotFoundException(detail = mapOf("gameEntryId" to command.gameEntryId))

        when (gameEntry.status) {
            GameEntryStatus.ALIVE -> gameEntry.sitOut()
            GameEntryStatus.SIT_OUT -> gameEntry.returnToGame()
        }
        logger.info { "Sit-out toggled: gameEntryId=${command.gameEntryId}, status=${gameEntry.status}" }
    }
}

data class ToggleSitOutCommand(
    val gameEntryId: Long
)
