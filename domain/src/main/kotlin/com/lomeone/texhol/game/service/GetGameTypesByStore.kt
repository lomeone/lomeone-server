package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.repository.GameTypeRepository
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetGameTypesByStore(
    private val storeRepository: StoreRepository,
    private val gameTypeRepository: GameTypeRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetGameTypesByStoreCommand): List<GameType> {
        val store = storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))

        return gameTypeRepository.findByStore(store)
    }
}

data class GetGameTypesByStoreCommand(
    val storeId: Long
)
