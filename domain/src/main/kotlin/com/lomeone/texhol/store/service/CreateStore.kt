package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.exception.StoreNameAlreadyExistException
import com.lomeone.texhol.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateStore(
    private val storeRepository: StoreRepository
) {
    @Transactional
    operator fun invoke(command: CreateStoreCommand): CreateStoreResult {
        val store = Store(
            name = command.name,
            location = command.location,
            address = command.address,
            imageUrl = command.imageUrl
        )
        verifyDuplicate(store)
        val savedStore = storeRepository.save(store)
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
