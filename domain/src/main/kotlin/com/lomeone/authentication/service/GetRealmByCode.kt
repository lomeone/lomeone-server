package com.lomeone.authentication.service

import com.lomeone.authentication.entity.Realm
import com.lomeone.authentication.entity.RealmStatus
import com.lomeone.authentication.exception.RealmNotFoundException
import com.lomeone.authentication.repository.RealmRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetRealmByCode(
    private val realmRepository: com.lomeone.authentication.repository.RealmRepository,
) {
    @Transactional(readOnly = true)
    fun execute(query: com.lomeone.authentication.service.GetRealmByCodeQuery): com.lomeone.authentication.service.GetRealmByCodeResult {
        val realm = getRealm(query.code)

        return _root_ide_package_.com.lomeone.authentication.service.GetRealmByCodeResult(
            code = realm.code,
            name = realm.name,
            status = realm.status
        )
    }

    private fun getRealm(code: String): com.lomeone.authentication.entity.Realm =
        realmRepository.findByCode(code) ?: throw _root_ide_package_.com.lomeone.authentication.exception.RealmNotFoundException(
            mapOf("code" to code)
        )
}

data class GetRealmByCodeQuery(
    val code: String
)

data class GetRealmByCodeResult(
    val code: String,
    val name: String,
    val status: com.lomeone.authentication.entity.RealmStatus
)
