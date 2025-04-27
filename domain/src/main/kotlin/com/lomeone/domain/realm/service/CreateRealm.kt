package com.lomeone.domain.realm.service

import com.lomeone.domain.realm.entity.Realm
import com.lomeone.domain.realm.exception.RealmAlreadyExistException
import com.lomeone.domain.realm.repository.RealmRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateRealm(
    private val realmRepository: RealmRepository,
) {
    @Transactional
    fun execute(command: CreateRealmCommand): CreateRealmResult {
        ensureNotDuplicateRealm(command.code)

        val realm = realmRepository.save(
            Realm(
                name = command.name,
                code = command.code
            )
        )

        return CreateRealmResult(realm.id)
    }

    private fun ensureNotDuplicateRealm(code: String?) {
        code != null && realmRepository.findByCode(code) != null && throw RealmAlreadyExistException(mapOf("code" to code))
    }
}

data class CreateRealmCommand(
    val name: String,
    val code: String? = null
)

data class CreateRealmResult(
    val realmId: Long
)
