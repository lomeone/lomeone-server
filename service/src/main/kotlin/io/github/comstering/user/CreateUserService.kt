package io.github.comstering.user

import io.github.comstering.user.entity.AccountType
import io.github.comstering.user.entity.Email
import io.github.comstering.user.entity.User
import io.github.comstering.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import javax.validation.constraints.NotBlank

@Service
class CreateUserService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(request: CreateUserServiceRequest): CreateUserServiceResponse {
        ensureUserNotExists(request.firebaseUserToken)

        val user = userRepository.save(
            User(
                firebaseUserToken = request.firebaseUserToken,
                name = request.name,
                nickname = request.nickname,
                email = Email(request.email),
                birthday = request.birthday,
                accountType = AccountType.valueOf(request.accountType)
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
    val birthday: LocalDate,
    @field:NotBlank val accountType: String
)

data class CreateUserServiceResponse(
    val id: Long?,
    val firebaseUserToken: String,
    val name: String,
    val nickname: String,
    val email: String,
    val birthday: LocalDate,
    val accountType: String
)
