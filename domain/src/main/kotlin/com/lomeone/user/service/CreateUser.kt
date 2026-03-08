package com.lomeone.user.service

import com.lomeone.common.entity.Email
import com.lomeone.user.entity.User
import com.lomeone.user.exception.UserEmailAlreadyExistsException
import com.lomeone.user.exception.UserPhoneNumberAlreadyExistsException
import com.lomeone.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import jakarta.validation.constraints.NotBlank

@Service
class CreateUser(
    private val userRepository: UserRepository,
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

        return CreateUserResult(
            userToken = savedUser.userToken
        )
    }

    private fun verifyDuplicate(user: User) {
        userRepository.findByEmail(user.email.value) != null
                && throw UserEmailAlreadyExistsException(detail = mapOf("email" to user.email))
        userRepository.findByPhoneNumber(user.phoneNumber) != null
                && throw UserPhoneNumberAlreadyExistsException(detail = mapOf("phone_number" to user.phoneNumber))
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
