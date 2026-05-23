package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.GameSessionStatus
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetGameSessionsByStore(
    private val storeRepository: StoreRepository,
    private val gameSessionRepository: GameSessionRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetGameSessionsByStoreCommand): List<GameSession> {
        logger.info { "Getting game sessions: storeId=${command.storeId}, status=${command.status}" }
        val store = storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))

        val result = if (command.status != null) {
            gameSessionRepository.findByStoreAndStatus(store, command.status)
        } else {
            gameSessionRepository.findByStore(store)
        }
        logger.info { "Game sessions found: count=${result.size}" }
        return result
    }
}

data class GetGameSessionsByStoreCommand(
    val storeId: Long,
    val status: GameSessionStatus? = null
)
