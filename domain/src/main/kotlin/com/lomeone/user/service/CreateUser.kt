package com.lomeone.user.service

import com.lomeone.common.entity.Email
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import jakarta.validation.constraints.NotBlank

@Service
class CreateUser(
    private val userRepository: com.lomeone.user.repository.UserRepository,
    private val associateAuthenticationToUser: com.lomeone.authentication.service.AssociateAuthenticationToUser,
) {
    @Transactional
    fun execute(command: com.lomeone.user.service.CreateUserCommand): com.lomeone.user.service.CreateUserResult {
        val (name, nickname, email, phoneNumber, birthday, authenticationUid, realmCode) = command

        val user = _root_ide_package_.com.lomeone.user.entity.User(
            name = name,
            nickname = nickname,
            email = Email(email),
            phoneNumber = phoneNumber,
            birthday = birthday
        )

        verifyDuplicate(user)

        val savedUser = userRepository.save(user)

        associateAuthenticationToUser.execute(
            _root_ide_package_.com.lomeone.authentication.service.AssociateAuthenticationToUserCommand(
                userToken = savedUser.userToken,
                uid = authenticationUid,
                realmCode = realmCode
            )
        )

        return _root_ide_package_.com.lomeone.user.service.CreateUserResult(
            userToken = savedUser.userToken
        )
    }

    private fun verifyDuplicate(user: com.lomeone.user.entity.User) {
        userRepository.findByEmail(user.email.value) != null
                && throw _root_ide_package_.com.lomeone.user.exception.UserEmailAlreadyExistsException(mapOf("email" to user.email))
        userRepository.findByPhoneNumber(user.phoneNumber) != null
                && throw _root_ide_package_.com.lomeone.user.exception.UserPhoneNumberAlreadyExistsException(mapOf("phone_number" to user.phoneNumber))
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
