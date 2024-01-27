package com.lomeone.domain.user.service

import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import jakarta.validation.constraints.NotBlank

@Service
class CreateUserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun createUser(command: CreateUserCommand): CreateUserResult {
        val (name, nickname, email, phoneNumber, birthday) = command

        val user = userRepository.save(
            User(
                name = name,
                nickname = nickname,
                email = Email(email),
                phoneNumber = phoneNumber,
                birthday = birthday
            )
        )

        return CreateUserResult(
            id = user.id,
            userToken = user.userToken,
            name = user.name,
            nickname = user.nickname,
            email = user.email.value,
            phoneNumber = user.phoneNumber,
            birthday = user.birthday
        )
    }
}

data class CreateUserCommand(
    @field:NotBlank val name: String,
    @field:NotBlank val nickname: String,
    @field:NotBlank val email: String,
    @field:NotBlank val phoneNumber: String,
    val birthday: LocalDate
)

data class CreateUserResult(
    val id: Long,
    val userToken: String,
    val name: String,
    val nickname: String,
    val email: String,
    val phoneNumber: String,
    val birthday: LocalDate
)
