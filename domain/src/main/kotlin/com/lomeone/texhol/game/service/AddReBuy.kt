package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameEntryNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class AddReBuy(
    private val gameEntryRepository: GameEntryRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: AddReBuyCommand) {
        logger.info { "Adding re-buy: gameEntryId=${command.gameEntryId}, paymentMethod=${command.paymentMethod}" }
        val gameEntry = gameEntryRepository.findByIdOrNull(command.gameEntryId)
            ?: throw GameEntryNotFoundException(detail = mapOf("gameEntryId" to command.gameEntryId))

        gameEntry.addReBuy(command.paymentMethod)
        logger.info { "Re-buy added: gameEntryId=${command.gameEntryId}" }
    }
}

data class AddReBuyCommand(
    val gameEntryId: Long,
    val paymentMethod: PaymentMethod
)
