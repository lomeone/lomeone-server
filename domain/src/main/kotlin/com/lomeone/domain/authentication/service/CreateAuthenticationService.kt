package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateAuthenticationService(
    private val authenticationRepository: AuthenticationRepository
) {
    @Transactional
    fun createAuthentication(command: CreateAuthenticationCommand): CreateAuthenticationResult {
        verifyDuplicate(command)

        val account = authenticationRepository.save(
            Authentication(
                uid = command.uid,
                email = Email(command.email),
                password = command.password,
                provider = command.provider,
                user = command.user
            )
        )

        return CreateAuthenticationResult(account.uid)
    }

    private fun verifyDuplicate(command: CreateAuthenticationCommand) {
        val emailAuthentication = authenticationRepository.findByEmailAndProvider(email = command.email, provider = command.provider)
        val uidAuthentication = authenticationRepository.findByUid(command.uid)

        if (emailAuthentication != null || uidAuthentication != null) {
            throw Exception()
        }
    }
}

data class CreateAuthenticationCommand(
    val email: String,
    val password: String?,
    val provider: AuthProvider,
    val uid: String = UUID.randomUUID().toString(),
    val user: User
)

data class CreateAuthenticationResult(
    val uid: String
)
