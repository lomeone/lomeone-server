package com.lomeone.user.service

import com.lomeone.user.entity.DeletionRequest
import com.lomeone.user.entity.DeletionStatus
import com.lomeone.user.exception.DeletionRequestNotFoundException
import com.lomeone.user.exception.UserNotFoundException
import com.lomeone.user.repository.DeletionRequestRepository
import com.lomeone.user.repository.UserRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RestoreUser(
    private val userRepository: UserRepository,
    private val deletionRequestRepository: DeletionRequestRepository
) {
    @Transactional
    fun execute(command: RestoreUserCommand): RestoreUserResult {
        val (userToken) = command

        val user = getUser(userToken)
        user.restore()

        val deletionRequest = getDeletionRequest(userToken)
        deletionRequest.restore()

        return RestoreUserResult(user.userToken)
    }

    private fun getDeletionRequest(userToken: String): DeletionRequest =
        deletionRequestRepository.findByUserTokenAndStatus(userToken, DeletionStatus.REQUEST)
            ?: throw DeletionRequestNotFoundException(mapOf("user_token" to userToken))

    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken) ?: throw UserNotFoundException(
            detail = mapOf("user_token" to userToken)
        )
}

data class RestoreUserCommand(
    @field:NotBlank val userToken: String
)

data class RestoreUserResult(
    val userToken: String
)
