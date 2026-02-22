package com.lomeone.user.service

import com.lomeone.user.entity.DeletionRequest
import com.lomeone.user.entity.DeletionStatus
import com.lomeone.user.exception.DeletionRequestAlreadyExistsException
import com.lomeone.user.exception.UserNotFoundException
import com.lomeone.user.repository.DeletionRequestRepository
import com.lomeone.user.repository.UserRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class RequestUserDeletion(
    private val userRepository: com.lomeone.user.repository.UserRepository,
    private val deletionRequestRepository: com.lomeone.user.repository.DeletionRequestRepository
) {
    @Transactional
    fun execute(command: com.lomeone.user.service.DeletionRequestUserCommand): com.lomeone.user.service.DeletionRequestUserResult {
        val (userToken, reason) = command

        verifyDuplicate(userToken)

        val user = getUser(userToken)

        user.deleteRequest()

        val deletionRequest = deletionRequestRepository.save(
            _root_ide_package_.com.lomeone.user.entity.DeletionRequest(
                userToken = userToken,
                reason = reason
            )
        )

        return _root_ide_package_.com.lomeone.user.service.DeletionRequestUserResult(
            userToken = deletionRequest.userToken,
            deletionCompletedAt = deletionRequest.createdAt.toLocalDate()
        )
    }

    private fun verifyDuplicate(userToken: String) {
        val deletionRequest = deletionRequestRepository.findByUserTokenAndStatus(
            userToken = userToken,
            status = _root_ide_package_.com.lomeone.user.entity.DeletionStatus.REQUEST
        )

        if (deletionRequest != null) {
            throw _root_ide_package_.com.lomeone.user.exception.DeletionRequestAlreadyExistsException(mapOf("user_token" to userToken))
        }
    }

    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken)
        ?: throw _root_ide_package_.com.lomeone.user.exception.UserNotFoundException(mapOf("user_token" to userToken))
}

data class DeletionRequestUserCommand(
    @field:NotBlank val userToken: String,
    val reason: String
)

data class DeletionRequestUserResult(
    val userToken: String,
    val deletionCompletedAt: LocalDate
)
