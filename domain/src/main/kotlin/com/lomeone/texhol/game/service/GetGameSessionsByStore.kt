package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.GameSessionStatus
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetGameSessionsByStore(
    private val storeRepository: StoreRepository,
    private val gameSessionRepository: GameSessionRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetGameSessionsByStoreCommand): List<GameSession> {
        val store = storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))

        return if (command.status != null) {
            gameSessionRepository.findByStoreAndStatus(store, command.status)
        } else {
            gameSessionRepository.findByStore(store)
        }
    }
}

data class GetGameSessionsByStoreCommand(
    val storeId: Long,
    val status: GameSessionStatus? = null
)
