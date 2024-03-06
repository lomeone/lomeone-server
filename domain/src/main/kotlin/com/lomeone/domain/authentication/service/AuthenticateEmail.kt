package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthenticateEmail(
    private val authenticationRepository: AuthenticationRepository
): Authenticate<AuthenticateEmailCommand> {
    @Transactional(readOnly = true)
    override fun authenticate(command: AuthenticateEmailCommand): Authentication {
        val (email, password) = command
        return authenticationRepository.findByEmailAndPassword(email, password) ?: throw Exception()
    }
}

data class AuthenticateEmailCommand(
    val email: String,
    val password: String
)
