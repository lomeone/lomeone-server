package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetStore(
    private val storeRepository: StoreRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(command: GetStoreCommand): Store {
        logger.info { "Getting store: storeId=${command.storeId}" }
        return storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))
    }
}

data class GetStoreCommand(
    val storeId: Long
)
