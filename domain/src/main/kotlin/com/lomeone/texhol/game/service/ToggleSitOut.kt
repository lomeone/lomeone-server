package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntryStatus
import com.lomeone.texhol.game.exception.GameEntryNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ToggleSitOut(
    private val gameEntryRepository: GameEntryRepository
) {
    @Transactional
    operator fun invoke(command: ToggleSitOutCommand) {
        val gameEntry = gameEntryRepository.findByIdOrNull(command.gameEntryId)
            ?: throw GameEntryNotFoundException(detail = mapOf("gameEntryId" to command.gameEntryId))

        when (gameEntry.status) {
            GameEntryStatus.ALIVE -> gameEntry.sitOut()
            GameEntryStatus.SIT_OUT -> gameEntry.returnToGame()
        }
    }
}

data class ToggleSitOutCommand(
    val gameEntryId: Long
)
