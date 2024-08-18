package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.exception.AuthenticationAlreadyExistsException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import com.lomeone.util.security.authentication.PasswordUtils.checkPasswordValidity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateAuthenticationService(
    private val authenticationRepository: AuthenticationRepository,
    private val bCryptPasswordEncoder: BCryptPasswordEncoder
) {
    @Transactional
    fun createAuthentication(command: CreateAuthenticationCommand): CreateAuthenticationResult {
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

        return CreateAuthenticationResult(authentication.uid)
    }

    private fun verifyDuplicate(command: CreateAuthenticationCommand) {
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

data class CreateAuthenticationCommand(
    val email: String,
    val password: String? = null,
    val provider: AuthProvider,
    val uid: String = UUID.randomUUID().toString(),
    val user: User
)

data class CreateAuthenticationResult(
    val uid: String
)
