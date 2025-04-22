package com.lomeone.domain.user.service

import com.lomeone.domain.authentication.service.AssociateAuthenticationToUser
import com.lomeone.domain.authentication.service.AssociateAuthenticationToUserCommand
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.exception.UserEmailAlreadyExistsException
import com.lomeone.domain.user.exception.UserPhoneNumberAlreadyExistsException
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import jakarta.validation.constraints.NotBlank

@Service
class CreateUser(
    private val userRepository: UserRepository,
    private val associateAuthenticationToUser: AssociateAuthenticationToUser,
) {
    @Transactional
    fun execute(command: CreateUserCommand): CreateUserResult {
        val (name, nickname, email, phoneNumber, birthday, authenticationUid, realmCode) = command

        val user = User(
            name = name,
            nickname = nickname,
            email = Email(email),
            phoneNumber = phoneNumber,
            birthday = birthday
        )

        verifyDuplicate(user)

        val savedUser = userRepository.save(user)

        associateAuthenticationToUser.execute(AssociateAuthenticationToUserCommand(
            userToken = savedUser.userToken,
            uid = authenticationUid,
            realmCode = realmCode
        ))

        return CreateUserResult(
            userToken = savedUser.userToken
        )
    }

    private fun verifyDuplicate(user: User) {
        userRepository.findByEmail(user.email.value) != null
                && throw UserEmailAlreadyExistsException(mapOf("email" to user.email))
        userRepository.findByPhoneNumber(user.phoneNumber) != null
                && throw UserPhoneNumberAlreadyExistsException(mapOf("phone_number" to user.phoneNumber))
    }
}

data class CreateUserCommand(
    @field:NotBlank val name: String,
    @field:NotBlank val nickname: String,
    @field:NotBlank val email: String,
    @field:NotBlank val phoneNumber: String,
    val birthday: LocalDate,
    @field:NotBlank val authenticationUid: String,
    @field:NotBlank val realmCode: String
)

data class CreateUserResult(
    val userToken: String
)
