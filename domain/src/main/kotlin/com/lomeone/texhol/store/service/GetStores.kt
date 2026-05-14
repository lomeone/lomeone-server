package com.lomeone.texhol.store.service

import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.repository.StoreRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetStores(
    private val storeRepository: StoreRepository
) {
    @Transactional(readOnly = true)
    operator fun invoke(): List<Store> {
        return storeRepository.findAll()
    }
}
