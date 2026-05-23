package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.Game
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.game.exception.GameNameAlreadyExistException
import com.lomeone.texhol.game.repository.GameRepository
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class CreateGame(
    private val storeRepository: StoreRepository,
    private val gameRepository: GameRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: CreateGameCommand): CreateGameResult {
        logger.info { "Creating game: storeId=${command.storeId}, name=${command.name}, scheduleType=${command.scheduleType}" }
        val store = storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))

        if (gameRepository.existsByStoreAndName(store, command.name)) {
            throw GameNameAlreadyExistException(
                detail = mapOf("storeId" to command.storeId, "name" to command.name)
            )
        }

        val game = Game(
            store = store,
            name = command.name,
            scheduleType = command.scheduleType,
            dayOfWeek = command.dayOfWeek,
            description = command.description
        )
        val savedGame = gameRepository.save(game)
        logger.info { "Game created: id=${savedGame.id}, name=${savedGame.name}" }

        return CreateGameResult(id = savedGame.id)
    }
}

data class CreateGameCommand(
    val storeId: Long,
    val name: String,
    val scheduleType: ScheduleType,
    val dayOfWeek: Int?,
    val description: String?
)

data class CreateGameResult(
    val id: Long
)
