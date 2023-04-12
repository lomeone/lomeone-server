package com.lomeone.domain.user.usecase

import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.constraints.NotBlank

@Service
class DeleteUser(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(request: DeleteUserRequest): DeleteUserResponse {
        val user = getUser(request.userToken)

        user.inactivate()

        return DeleteUserResponse(user.userToken)
    }
    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken) ?: throw Exception("User not found")
}

data class DeleteUserRequest(
    @field:NotBlank val userToken: String
)

data class DeleteUserResponse(
    val userToken: String
)
