package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetPaymentStatsByGame(
    private val gameSessionRepository: GameSessionRepository,
    private val gameEntryRepository: GameEntryRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetPaymentStatsByGameCommand): PaymentStats {
        val gameSession = gameSessionRepository.findByIdOrNull(command.gameSessionId)
            ?: throw GameSessionNotFoundException(detail = mapOf("gameSessionId" to command.gameSessionId))

        val gameEntries = gameEntryRepository.findByGameSession(gameSession)
        val buyInRecords = gameEntries.flatMap { it.buyInRecords }

        val cashCount = buyInRecords.count { it.paymentMethod == PaymentMethod.CASH }
        val cardCount = buyInRecords.count { it.paymentMethod == PaymentMethod.CARD }
        val pointCount = buyInRecords.count { it.paymentMethod == PaymentMethod.POINTS }

        return PaymentStats(
            cash = cashCount,
            card = cardCount,
            points = pointCount,
            total = buyInRecords.size
        )
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
