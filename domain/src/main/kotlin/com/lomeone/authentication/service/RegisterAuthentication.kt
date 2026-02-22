package com.lomeone.authentication.service

import com.lomeone.common.entity.Email
import com.lomeone.eunoia.kotlin.util.security.authentication.PasswordUtils.checkPasswordValidity
import com.lomeone.eunoia.kotlin.util.string.StringUtils.generateRandomString
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterAuthentication(
    private val authenticationRepository: com.lomeone.authentication.repository.AuthenticationRepository,
    private val realmRepository: com.lomeone.authentication.repository.RealmRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @Transactional
    fun execute(command: com.lomeone.authentication.service.RegisterAuthenticationCommand): com.lomeone.authentication.service.RegisterAuthenticationResult {
        val (email, password, provider, uid, realmCode) = command

        val realm = getRealm(realmCode)

        verifyDuplicate(email, provider, uid, realm)

        val authentication = authenticationRepository.save(
            _root_ide_package_.com.lomeone.authentication.entity.Authentication(
                uid = uid,
                email = Email(email),
                password = encodePassword(password),
                provider = provider,
                realm = realm
            )
        )

        return _root_ide_package_.com.lomeone.authentication.service.RegisterAuthenticationResult(authentication.uid)
    }

    private fun getRealm(realmCode: String): com.lomeone.authentication.entity.Realm =
        realmRepository.findByCode(realmCode) ?: throw _root_ide_package_.com.lomeone.authentication.exception.RealmNotFoundException(
            mapOf("code" to realmCode)
        )

    private fun verifyDuplicate(email: String, provider: com.lomeone.authentication.entity.AuthProvider, uid: String, realm: com.lomeone.authentication.entity.Realm) {
        authenticationRepository.findByEmailAndProviderAndRealm(email, provider, realm) != null
                && throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationAlreadyExistsException(
            detail = mapOf("email" to email, "provider" to provider, "realm" to realm.code)
        )
        authenticationRepository.findByUid(uid) != null
                && throw _root_ide_package_.com.lomeone.authentication.exception.AuthenticationAlreadyExistsException(
            mapOf("uid" to uid)
        )
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
    val provider: com.lomeone.authentication.entity.AuthProvider,
    val uid: String = generateRandomString((('0'..'9') + ('a'..'z') + ('A'..'Z')).toSet(), 8),
    val realmCode: String
)

data class RegisterAuthenticationResult(
    val uid: String
)
