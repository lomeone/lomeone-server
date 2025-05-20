package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Realm
import com.lomeone.domain.authentication.entity.RealmStatus
import com.lomeone.domain.authentication.exception.RealmNotFoundException
import com.lomeone.domain.authentication.repository.RealmRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetRealmByCode(
    private val realmRepository: RealmRepository,
) {
    @Transactional(readOnly = true)
    fun execute(query: GetRealmByCodeQuery): GetRealmByCodeResult {
        val realm = getRealm(query.code)

        return GetRealmByCodeResult(
            code = realm.code,
            name = realm.name,
            status = realm.status
        )
    }

    private fun getRealm(code: String): Realm =
        realmRepository.findByCode(code) ?: throw RealmNotFoundException(mapOf("code" to code))
}

data class GetRealmByCodeQuery(
    val code: String
)

data class GetRealmByCodeResult(
    val code: String,
    val name: String,
    val status: RealmStatus
)
