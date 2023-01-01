package io.github.comstering.user

import io.github.comstering.user.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import javax.validation.constraints.NotBlank

@Service
class CreateUserService(
    private val userRepository: UserRepository
) {
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
    val id: Long,
    val firebaseUserToken: String,
    val name: String,
    val nickname: String,
    val email: String,
    val birthday: LocalDate,
    val accountType: String
)
