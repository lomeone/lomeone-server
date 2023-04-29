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
    fun execute(request: DeleteUserCommand): DeleteUserResult {
        val user = getUser(request.userToken)

        user.inactivate()

        return DeleteUserResult(user.userToken)
    }
    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken) ?: throw Exception("User not found")
}

data class DeleteUserCommand(
    @field:NotBlank val userToken: String
)

data class DeleteUserResult(
    val userToken: String
)
