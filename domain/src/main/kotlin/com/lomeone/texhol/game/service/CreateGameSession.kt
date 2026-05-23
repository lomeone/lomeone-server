package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.exception.GameNotFoundException
import com.lomeone.texhol.game.exception.GameSessionAlreadyExistException
import com.lomeone.texhol.game.exception.GameStoreMismatchException
import com.lomeone.texhol.game.repository.GameRepository
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class CreateGameSession(
    private val storeRepository: StoreRepository,
    private val gameRepository: GameRepository,
    private val gameSessionRepository: GameSessionRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: CreateGameSessionCommand): CreateGameSessionResult {
        logger.info { "Creating game session: storeId=${command.storeId}, gameId=${command.gameId}, session=${command.session}" }
        val store = storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))

        val game = gameRepository.findByIdOrNull(command.gameId)
            ?: throw GameNotFoundException(detail = mapOf("gameId" to command.gameId))

        if (!game.belongsTo(store)) {
            throw GameStoreMismatchException(
                detail = mapOf("storeId" to command.storeId, "gameId" to command.gameId)
            )
        }

        if (gameSessionRepository.existsByStoreAndGameAndSession(store, game, command.session)) {
            throw GameSessionAlreadyExistException(
                detail = mapOf("storeId" to command.storeId, "gameId" to command.gameId, "session" to command.session)
            )
        }

        val gameSession = GameSession.create(
            store = store,
            game = game,
            session = command.session
        )
        val savedGameSession = gameSessionRepository.save(gameSession)
        logger.info { "Game session created: id=${savedGameSession.id}" }

        return CreateGameSessionResult(id = savedGameSession.id)
    }
}

data class CreateGameSessionCommand(
    val storeId: Long,
    val gameId: Long,
    val session: Int
)

data class CreateGameSessionResult(
    val id: Long
)
