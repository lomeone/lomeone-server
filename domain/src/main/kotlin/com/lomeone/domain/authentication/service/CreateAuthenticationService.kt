package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.common.entity.Email
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class CreateAuthenticationService(
    private val authenticationRepository: AuthenticationRepository
) {
    @Transactional
    fun createAuthentication(command: CreateAccountCommand): CreateAccountResult {
        verifyDuplicate(command)

        val account = authenticationRepository.save(
            Authentication(
                uid = command.uid,
                email = Email(command.email),
                password = command.password,
                provider = command.provider
            )
        )

        return CreateAccountResult(account.uid)
    }

    private fun verifyDuplicate(command: CreateAccountCommand) {
        val emailAuthentication = authenticationRepository.findByEmailAndProvider(email = command.email, provider = command.provider)
        val uidAuthentication = authenticationRepository.findByUid(command.uid)

        if (emailAuthentication != null || uidAuthentication != null) {
            throw Exception()
        }
    }
}

data class CreateAccountCommand(
    val email: String,
    val provider: AuthProvider,
    val uid: String = UUID.randomUUID().toString(),
    val password: String?
)

data class CreateAccountResult(
    val uid: String
)
