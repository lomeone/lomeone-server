package com.lomeone.authentication.service

import com.lomeone.authentication.entity.Realm
import com.lomeone.authentication.exception.RealmAlreadyExistException
import com.lomeone.authentication.repository.RealmRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateRealm(
    private val realmRepository: com.lomeone.authentication.repository.RealmRepository,
) {
    @Transactional
    fun execute(command: com.lomeone.authentication.service.CreateRealmCommand): com.lomeone.authentication.service.CreateRealmResult {
        ensureNotDuplicateRealm(command.code)

        val realm = realmRepository.save(
            _root_ide_package_.com.lomeone.authentication.entity.Realm(
                name = command.name,
                code = command.code
            )
        )

        return _root_ide_package_.com.lomeone.authentication.service.CreateRealmResult(realm.code)
    }

    private fun ensureNotDuplicateRealm(code: String?) {
        code != null && realmRepository.findByCode(code) != null && throw _root_ide_package_.com.lomeone.authentication.exception.RealmAlreadyExistException(
            mapOf("code" to code)
        )
    }
}

data class CreateRealmCommand(
    val name: String,
    val code: String? = null
)

data class CreateRealmResult(
    val realmCode: String
)
