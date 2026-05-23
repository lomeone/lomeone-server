package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetPaymentStatsByGame(
    private val gameSessionRepository: GameSessionRepository,
    private val gameEntryRepository: GameEntryRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetPaymentStatsByGameCommand): PaymentStats {
        logger.info { "Getting payment stats: gameSessionId=${command.gameSessionId}" }
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))

        val gameEntries = gameEntryRepository.findByGameSession(gameSession)
        val buyInRecords = gameEntries.flatMap { it.buyInRecords }

        val cashCount = buyInRecords.count { it.paymentMethod == PaymentMethod.CASH }
        val cardCount = buyInRecords.count { it.paymentMethod == PaymentMethod.CARD }
        val pointCount = buyInRecords.count { it.paymentMethod == PaymentMethod.POINTS }

        val stats = PaymentStats(
            cash = cashCount,
            card = cardCount,
            points = pointCount,
            total = buyInRecords.size
        )
        logger.info { "Payment stats computed: cash=${stats.cash}, card=${stats.card}, points=${stats.points}, total=${stats.total}" }
        return stats
    }
}

data class GetPaymentStatsByGameCommand(
    val gameSessionId: Long
)

data class PaymentStats(
    val cash: Int,
    val card: Int,
    val points: Int,
    val total: Int
)
