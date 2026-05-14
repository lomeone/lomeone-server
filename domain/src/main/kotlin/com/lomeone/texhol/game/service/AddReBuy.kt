package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameEntryNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddReBuy(
    private val gameEntryRepository: GameEntryRepository
) {
    @Transactional
    operator fun invoke(command: AddReBuyCommand) {
        val gameEntry = gameEntryRepository.findByIdOrNull(command.gameEntryId)
            ?: throw GameEntryNotFoundException(detail = mapOf("gameEntryId" to command.gameEntryId))

        gameEntry.addReBuy(command.paymentMethod)
    }
}

data class AddReBuyCommand(
    val gameEntryId: Long,
    val paymentMethod: PaymentMethod
)
