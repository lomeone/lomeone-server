package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.Game
import com.lomeone.texhol.game.repository.GameRepository
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetGamesByStore(
    private val storeRepository: StoreRepository,
    private val gameRepository: GameRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetGamesByStoreCommand): List<Game> {
        logger.info { "Getting games: storeId=${command.storeId}" }
        val store = storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))

        val games = gameRepository.findByStore(store)
        logger.info { "Games found: count=${games.size}" }
        return games
    }
}

data class GetGamesByStoreCommand(
    val storeId: Long
)
