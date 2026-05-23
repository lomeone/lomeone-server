package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.exception.StoreNameAlreadyExistException
import com.lomeone.texhol.store.repository.StoreRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class CreateStore(
    private val storeRepository: StoreRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: CreateStoreCommand): CreateStoreResult {
        logger.info { "Creating store: name=${command.name}, location=${command.location}" }
        val store = Store(
            name = command.name,
            location = command.location,
            address = command.address,
            imageUrl = command.imageUrl
        )
        verifyDuplicate(store)
        val savedStore = storeRepository.save(store)
        logger.info { "Store created: id=${savedStore.id}, name=${savedStore.name}" }
        return CreateStoreResult(
            id = savedStore.id,
            name = savedStore.name,
            location = savedStore.location,
            address = savedStore.address,
            imageUrl = savedStore.imageUrl
        )
    }

    private fun verifyDuplicate(store: Store) {
        if (storeRepository.findByName(store.name) != null) {
            throw StoreNameAlreadyExistException(detail = mapOf("name" to store.name))
        }
    }
}

data class CreateStoreCommand(
    val name: String,
    val location: String,
    val address: String?,
    val imageUrl: String
)

data class CreateStoreResult(
    val id: Long,
    val name: String,
    val location: String,
    val address: String?,
    val imageUrl: String
)
