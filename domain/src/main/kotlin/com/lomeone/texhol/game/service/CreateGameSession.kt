package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.exception.GameSessionAlreadyExistException
import com.lomeone.texhol.game.exception.GameTypeNotFoundException
import com.lomeone.texhol.game.exception.GameTypeStoreMismatchException
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.game.repository.GameTypeRepository
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateGameSession(
    private val storeRepository: StoreRepository,
    private val gameTypeRepository: GameTypeRepository,
    private val gameSessionRepository: GameSessionRepository
) {
    @Transactional
    operator fun invoke(command: CreateGameSessionCommand): CreateGameSessionResult {
        val store = storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))

        val gameType = gameTypeRepository.findByIdOrNull(command.gameTypeId)
            ?: throw GameTypeNotFoundException(detail = mapOf("gameTypeId" to command.gameTypeId))

        if (!gameType.belongsTo(store)) {
            throw GameTypeStoreMismatchException(
                detail = mapOf("storeId" to command.storeId, "gameTypeId" to command.gameTypeId)
            )
        }

        if (gameSessionRepository.existsByStoreAndGameTypeAndSession(store, gameType, command.session)) {
            throw GameSessionAlreadyExistException(
                detail = mapOf("storeId" to command.storeId, "gameTypeId" to command.gameTypeId, "session" to command.session)
            )
        }

        val gameSession = GameSession.create(
            store = store,
            gameType = gameType,
            session = command.session
        )
        val savedGameSession = gameSessionRepository.save(gameSession)

        return CreateGameSessionResult(id = savedGameSession.id)
    }
}

data class CreateGameSessionCommand(
    val storeId: Long,
    val gameTypeId: Long,
    val session: Int
)

data class CreateGameSessionResult(
    val id: Long
)
