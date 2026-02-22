package com.lomeone.authentication.service

import com.lomeone.authentication.exception.AuthenticationNotFoundException
import com.lomeone.authentication.repository.AuthenticationRepository
import com.lomeone.authentication.entity.Realm
import com.lomeone.authentication.exception.RealmNotFoundException
import com.lomeone.authentication.repository.RealmRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetAuthenticationByUidAndRealmCode(
    private val realmRepository: com.lomeone.authentication.repository.RealmRepository,
    private val authenticationRepository: com.lomeone.authentication.repository.AuthenticationRepository
) {
    @Transactional(readOnly = true)
    fun execute(query: com.lomeone.authentication.service.GetAuthenticationByUidAndRealmCodeQuery) {
        val (uid, realmCode) = query

        val realm = getRealm(realmCode)

        val authentication = getAuthentication(uid, realm)
    }

    private fun getRealm(realmCode: String): com.lomeone.authentication.entity.Realm =
        realmRepository.findByCode(realmCode)
            ?: throw _root_ide_package_.com.lomeone.authentication.exception.RealmNotFoundException(mapOf("code" to realmCode))

    private fun getAuthentication(uid: String, realm: com.lomeone.authentication.entity.Realm) =
        authenticationRepository.findByUidAndRealm(uid, realm)
            ?: throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationNotFoundException(
                mapOf(
                    "uid" to uid,
                    "realm" to realm
                )
            )
}

data class GetAuthenticationByUidAndRealmCodeQuery(
    val uid: String,
    val realmCode: String
)

data class GetAuthenticationByUidAndRealmCodeResult(
    val uid: String,
    val realmCode: String
)
