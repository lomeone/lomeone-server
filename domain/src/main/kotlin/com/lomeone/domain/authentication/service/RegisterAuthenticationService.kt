package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.exception.AuthenticationAlreadyExistsException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import com.lomeone.util.security.authentication.PasswordUtils.checkPasswordValidity
import com.lomeone.util.string.RandomStringUtil.generateRandomString
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterAuthenticationService(
    private val authenticationRepository: AuthenticationRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @Transactional
    fun registerAuthentication(command: RegisterAuthenticationCommand): RegisterAuthenticationResult {
        verifyDuplicate(command)

        val authentication = authenticationRepository.save(
            Authentication(
                uid = command.uid,
                email = Email(command.email),
                password = encodePassword(command.password),
                provider = command.provider,
                user = command.user
            )
        )

        return RegisterAuthenticationResult(authentication.uid)
    }

    private fun verifyDuplicate(command: RegisterAuthenticationCommand) {
        authenticationRepository.findByEmailAndProvider(email = command.email, provider = command.provider) != null
                && throw AuthenticationAlreadyExistsException(detail = mapOf("email" to command.email, "provider" to command.provider))
        authenticationRepository.findByUid(command.uid) != null
                && throw AuthenticationAlreadyExistsException(mapOf("uid" to command.uid))
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
    val user: User
)

data class RegisterAuthenticationResult(
    val uid: String
)
