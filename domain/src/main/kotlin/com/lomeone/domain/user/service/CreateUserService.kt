package com.lomeone.domain.user.service

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.service.CreateAuthenticationCommand
import com.lomeone.domain.authentication.service.CreateAuthenticationService
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.exception.UserEmailAlreadyExistsException
import com.lomeone.domain.user.exception.UserPhoneNumberAlreadyExistsException
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.util.UUID

@Service
class CreateUserService(
    private val userRepository: UserRepository,
    private val createAuthenticationService: CreateAuthenticationService
) {
    @Transactional
    fun createUser(command: CreateUserCommand): CreateUserResult {
        val (userInfo, authenticationInfo) = command
        val (name, nickname, email, phoneNumber, birthday) = userInfo

        verifyDuplicate(userInfo)

        val user = userRepository.save(
            User(
                name = name,
                nickname = nickname,
                email = Email(email),
                phoneNumber = phoneNumber,
                birthday = birthday
            )
        )

        val authenticationCommand = CreateAuthenticationCommand(
            email = authenticationInfo.email,
            password = authenticationInfo.password,
            provider = authenticationInfo.provider,
            uid = authenticationInfo.uid,
            user = user
        )

        val createAuthResult = createAuthenticationService.createAuthentication(authenticationCommand)

        return CreateUserResult(
            userToken = user.userToken,
            authenticationUid = createAuthResult.uid
        )
    }

    private fun verifyDuplicate(userInfo: CreateUserCommand.UserInfo) {
        userRepository.findByEmail(userInfo.email) != null
                && throw UserEmailAlreadyExistsException(mapOf("email" to userInfo.email))
        userRepository.findByPhoneNumber(userInfo.phoneNumber) != null
                && throw UserPhoneNumberAlreadyExistsException(mapOf("phoneNumber" to userInfo.phoneNumber))
    }
}

data class CreateUserCommand(
    @field:NotNull val userInfo: UserInfo,
    @field:NotNull val authenticationInfo: AuthenticationInfo
) {
    data class UserInfo(
        @field:NotBlank val name: String,
        @field:NotBlank val nickname: String,
        @field:NotBlank val email: String,
        @field:NotBlank val phoneNumber: String,
        @field:NotNull val birthday: LocalDate,
    )

    data class AuthenticationInfo(
        @field:NotBlank val email: String,
        val password: String? = null,
        @field:NotNull val provider: AuthProvider,
        @field:NotBlank val uid: String = UUID.randomUUID().toString()
    )
}

data class CreateUserResult(
    val userToken: String,
    val authenticationUid: String
)
