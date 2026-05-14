package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetStore(
    private val storeRepository: StoreRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(command: GetStoreCommand): Store {
        return storeRepository.findByIdOrNull(command.storeId)
            ?: throw StoreNotFoundException(detail = mapOf("storeId" to command.storeId))
    }
}

data class GetStoreCommand(
    val storeId: Long
)
