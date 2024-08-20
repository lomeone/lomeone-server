package com.lomeone.domain.user.service

import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class DeleteUserRequestService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun deleteUser(command: DeleteUserRequestServiceCommand) {
        val (userToken) = command

        val user = getUser(userToken)

        userRepository.delete(user)
    }

    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken)
        ?: throw Exception("User not found")
}

data class DeleteUserRequestServiceCommand(
    val userToken: String
)

data class DeleteUserRequestServiceResult(
    val userToken: String,
    val deletionCompletedAt: LocalDate
)
