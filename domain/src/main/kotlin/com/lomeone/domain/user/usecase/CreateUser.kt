package com.lomeone.domain.user.usecase

import com.lomeone.domain.user.entity.AccountType
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime
import javax.validation.constraints.NotBlank

@Service
class CreateUser(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(request: CreateUserServiceRequest): CreateUserServiceResponse {
        val (userToken, name, nickname, email, birthday,photoUrl, accountType) = request

        ensureUserNotExists(userToken)

        val user = userRepository.save(
            User(
                userToken = userToken,
                name = name,
                nickname = nickname,
                email = email,
                birthday = birthday,
                photoUrl = photoUrl,
                accountType = AccountType.valueOf(accountType)
            )
        )

        return CreateUserServiceResponse(
            id = user.id,
            userToken = user.userToken,
            name = user.name,
            nickname = user.nickname,
            email = user.email.value,
            birthday = user.birthday,
            photoUrl = user.photoUrl,
            accountType = user.accountType.name
        )
    }

    private fun ensureUserNotExists(userToken: String) {
        userRepository.findByUserToken(userToken) != null
            && throw Exception("User already exists")
    }
}

data class CreateUserServiceRequest(
    @field:NotBlank val userToken: String,
    @field:NotBlank val name: String,
    @field:NotBlank val nickname: String,
    @field:NotBlank val email: String,
    val birthday: ZonedDateTime,
    @field:NotBlank val photoUrl: String,
    @field:NotBlank val accountType: String
)

data class CreateUserServiceResponse(
    val id: Long,
    val userToken: String,
    val name: String,
    val nickname: String,
    val email: String,
    val birthday: ZonedDateTime,
    val photoUrl: String,
    val accountType: String
)
