package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.game.exception.GameTypeNameAlreadyExistException
import com.lomeone.texhol.game.repository.GameTypeRepository
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateGameType(
    private val storeRepository: StoreRepository,
    private val gameTypeRepository: GameTypeRepository
) {
    @Transactional
    operator fun invoke(command: CreateGameTypeCommand): CreateGameTypeResult {
        val store = storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))

        if (gameTypeRepository.existsByStoreAndName(store, command.name)) {
            throw GameTypeNameAlreadyExistException(
                detail = mapOf("storeId" to command.storeId, "name" to command.name)
            )
        }

        val gameType = GameType(
            store = store,
            name = command.name,
            scheduleType = command.scheduleType,
            dayOfWeek = command.dayOfWeek,
            description = command.description
        )
        val savedGameType = gameTypeRepository.save(gameType)

        return CreateGameTypeResult(id = savedGameType.id)
    }
}

data class CreateGameTypeCommand(
    val storeId: Long,
    val name: String,
    val scheduleType: ScheduleType,
    val dayOfWeek: Int?,
    val description: String?
)

data class CreateGameTypeResult(
    val id: Long
)
