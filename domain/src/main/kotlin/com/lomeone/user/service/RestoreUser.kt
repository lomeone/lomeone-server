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
    private val userRepository: com.lomeone.user.repository.UserRepository,
    private val deletionRequestRepository: com.lomeone.user.repository.DeletionRequestRepository
) {
    @Transactional
    fun execute(command: com.lomeone.user.service.RestoreUserCommand): com.lomeone.user.service.RestoreUserResult {
        val (userToken) = command

        val user = getUser(userToken)
        user.restore()

        val deletionRequest = getDeletionRequest(userToken)
        deletionRequest.restore()

        return _root_ide_package_.com.lomeone.user.service.RestoreUserResult(user.userToken)
    }

    private fun getDeletionRequest(userToken: String): com.lomeone.user.entity.DeletionRequest =
        deletionRequestRepository.findByUserTokenAndStatus(userToken, _root_ide_package_.com.lomeone.user.entity.DeletionStatus.REQUEST)
            ?: throw _root_ide_package_.com.lomeone.user.exception.DeletionRequestNotFoundException(mapOf("user_token" to userToken))

    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken) ?: throw _root_ide_package_.com.lomeone.user.exception.UserNotFoundException(
            mapOf("user_token" to userToken)
        )
}

data class RestoreUserCommand(
    @field:NotBlank val userToken: String
)

data class RestoreUserResult(
    val userToken: String
)
