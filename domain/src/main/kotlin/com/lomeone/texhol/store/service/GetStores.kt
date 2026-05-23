package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.repository.StoreRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class GetStores(
    private val storeRepository: StoreRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional(readOnly = true)
    operator fun invoke(): List<Store> {
        logger.info { "Getting all stores" }
        val stores = storeRepository.findAll()
        logger.info { "Stores found: count=${stores.size}" }
        return stores
    }
}
