package io.github.comstering.domain.user.usecase

import io.github.comstering.domain.user.entity.AccountType
import io.github.comstering.domain.user.entity.User
import io.github.comstering.domain.user.repository.UserRepository
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
        val (firebaseUserToken, name, nickname, email, birthday, accountType) = request

        ensureUserNotExists(firebaseUserToken)

        val user = userRepository.save(
            User(
                firebaseUserToken = firebaseUserToken,
                name = name,
                nickname = nickname,
                email = email,
                birthday = birthday,
                accountType = AccountType.valueOf(accountType)
            )
        )

        return CreateUserServiceResponse(
            id = user.id,
            firebaseUserToken = user.firebaseUserToken,
            name = user.name,
            nickname = user.nickname,
            email = user.email.value,
            birthday = user.birthday,
            accountType = user.accountType.name
        )
    }

    private fun ensureUserNotExists(firebaseUserToken: String) {
        userRepository.findByFirebaseUserToken(firebaseUserToken) != null
            && throw Exception("User already exists")
    }
}

data class CreateUserServiceRequest(
    @field:NotBlank val firebaseUserToken: String,
    @field:NotBlank val name: String,
    @field:NotBlank val nickname: String,
    @field:NotBlank val email: String,
    val birthday: ZonedDateTime,
    @field:NotBlank val accountType: String
)

data class CreateUserServiceResponse(
    val id: Long,
    val firebaseUserToken: String,
    val name: String,
    val nickname: String,
    val email: String,
    val birthday: ZonedDateTime,
    val accountType: String
)
