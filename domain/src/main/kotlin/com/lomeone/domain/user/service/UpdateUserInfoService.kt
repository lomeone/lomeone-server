package com.lomeone.domain.user.service

import com.lomeone.domain.user.exception.UserNotFoundException
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class UpdateUserInfoService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(command: UpdateUserInfoServiceCommand): UpdateUserInfoServiceResult {
        val (userToken, name, nickname, birthday) = command

        val user = getUser(userToken)

        user.updateUserInfo(name, nickname, birthday)

        return UpdateUserInfoServiceResult(
            userToken = user.userToken,
            name = user.name,
            nickname = user.nickname,
            birthday = user.birthday
        )
    }

    private fun getUser(userToken: String) = userRepository.findByUserToken(userToken)
        ?: throw UserNotFoundException(mapOf("userToken" to userToken))
}

data class UpdateUserInfoServiceCommand(
    val userToken: String,
    val name: String,
    val nickname: String,
    val birthday: LocalDate
)

data class UpdateUserInfoServiceResult(
    val userToken: String,
    val name: String,
    val nickname: String,
    val birthday: LocalDate
)
