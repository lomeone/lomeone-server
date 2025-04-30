package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.exception.AuthenticationAlreadyExistsException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.realm.entity.Realm
import com.lomeone.domain.realm.repository.RealmRepository
import com.lomeone.eunoia.kotlin.util.security.authentication.PasswordUtils.checkPasswordValidity
import com.lomeone.eunoia.kotlin.util.string.StringUtils.generateRandomString
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterAuthentication(
    private val authenticationRepository: AuthenticationRepository,
    private val realmRepository: RealmRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @Transactional
    fun execute(command: RegisterAuthenticationCommand): RegisterAuthenticationResult {
        val (email, password, provider, uid, realmCode) = command

        val realm = getRealm(realmCode)

        verifyDuplicate(email, provider, uid, realm)

        val authentication = authenticationRepository.save(
            Authentication(
                uid = uid,
                email = Email(email),
                password = encodePassword(password),
                provider = provider,
                realm = realm
            )
        )

        return RegisterAuthenticationResult(authentication.uid)
    }

    private fun getRealm(realmCode: String): Realm {
        return realmRepository.findByCode(realmCode)
            ?: throw IllegalArgumentException("Realm not found with code: $realmCode")
    }

    private fun verifyDuplicate(email: String, provider: AuthProvider, uid: String, realm: Realm) {
        authenticationRepository.findByEmailAndProviderAndRealm(email, provider, realm) != null
                && throw AuthenticationAlreadyExistsException(detail = mapOf("email" to email, "provider" to provider, "realm" to realm.code))
        authenticationRepository.findByUid(uid) != null
                && throw AuthenticationAlreadyExistsException(mapOf("uid" to uid))
    }

    private fun encodePassword(password: String?): String? {
        if (password == null) return null
        checkPasswordValidity(password)
        return bCryptPasswordEncoder.encode(password)
    }
}

data class RegisterAuthenticationCommand(
    val email: String,
    val password: String? = null,
    val provider: AuthProvider,
    val uid: String = generateRandomString((('0'..'9') + ('a'..'z') + ('A'..'Z')).toSet(), 8),
    val realmCode: String
)

data class RegisterAuthenticationResult(
    val uid: String
)
