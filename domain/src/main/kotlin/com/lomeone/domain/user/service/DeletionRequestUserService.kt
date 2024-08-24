package com.lomeone.domain.user.service

import com.lomeone.domain.user.entity.DeletionRequest
import com.lomeone.domain.user.entity.DeletionStatus
import com.lomeone.domain.user.exception.DeletionRequestAlreadyExistsException
import com.lomeone.domain.user.repository.DeletionRequestRepository
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class DeletionRequestUserService(
    private val userRepository: UserRepository,
    private val deletionRequestRepository: DeletionRequestRepository
) {
    @Transactional
    fun deletionRequestUser(command: DeletionRequestUserServiceCommand): DeletionRequestUserServiceResult {
        val (userToken, reason) = command

        verifyDuplicate(userToken)

        val user = getUser(userToken)

        user.deleteRequest()

        val deletionRequest = deletionRequestRepository.save(DeletionRequest(userToken = userToken, reason = reason))

        return DeletionRequestUserServiceResult(
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
        ?: throw Exception("User not found")
}

data class DeletionRequestUserServiceCommand(
    val userToken: String,
    val reason: String
)

data class DeletionRequestUserServiceResult(
    val userToken: String,
    val deletionCompletedAt: LocalDate
)
