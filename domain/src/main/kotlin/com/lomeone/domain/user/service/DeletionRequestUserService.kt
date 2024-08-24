package com.lomeone.domain.user.service

import com.lomeone.domain.user.entity.DeletionRequest
import com.lomeone.domain.user.entity.DeletionStatus
import com.lomeone.domain.user.exception.DeletionRequestAlreadyExistsException
import com.lomeone.domain.user.exception.UserNotFoundException
import com.lomeone.domain.user.repository.DeletionRequestRepository
import com.lomeone.domain.user.repository.UserRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class DeletionRequestUserService(
    private val userRepository: UserRepository,
    private val deletionRequestRepository: DeletionRequestRepository
) {
    @Transactional
    fun deletionRequestUser(command: DeletionRequestUserCommand): DeletionRequestUserResult {
        val (userToken, reason) = command

        verifyDuplicate(userToken)

        val user = getUser(userToken)

        user.deleteRequest()

        val deletionRequest = deletionRequestRepository.save(DeletionRequest(userToken = userToken, reason = reason))

        return DeletionRequestUserResult(
            userToken = deletionRequest.userToken,
            deletionCompletedAt = deletionRequest.createdAt.toLocalDate()
        )
    }

    private fun verifyDuplicate(userToken: String) {
        val deletionRequest = deletionRequestRepository.findByUserTokenAndStatus(
            userToken = userToken,
            status = DeletionStatus.REQUEST
        )

        if (deletionRequest != null) {
            throw DeletionRequestAlreadyExistsException(mapOf("user_token" to userToken))
        }
    }

    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken)
        ?: throw UserNotFoundException(mapOf("user_token" to userToken))
}

data class DeletionRequestUserCommand(
    @field:NotBlank val userToken: String,
    val reason: String
)

data class DeletionRequestUserResult(
    val userToken: String,
    val deletionCompletedAt: LocalDate
)
