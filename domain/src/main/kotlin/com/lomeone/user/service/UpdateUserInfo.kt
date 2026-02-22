package com.lomeone.user.service

import com.lomeone.user.exception.UserNotFoundException
import com.lomeone.user.repository.UserRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Service
class UpdateUserInfo(
    private val userRepository: com.lomeone.user.repository.UserRepository
) {
    @Transactional
    fun execute(command: com.lomeone.user.service.UpdateUserInfoCommand): com.lomeone.user.service.UpdateUserInfoResult {
        val (userToken, name, nickname, birthday) = command

        val user = getUser(userToken)

        user.updateUserInfo(name, nickname, birthday)

        return _root_ide_package_.com.lomeone.user.service.UpdateUserInfoResult(
            userToken = user.userToken,
            name = user.name,
            nickname = user.nickname,
            birthday = user.birthday
        )
    }

    private fun getUser(userToken: String) = userRepository.findByUserToken(userToken)
        ?: throw _root_ide_package_.com.lomeone.user.exception.UserNotFoundException(mapOf("user_token" to userToken))
}

data class UpdateUserInfoCommand(
    @field:NotBlank val userToken: String,
    @field:NotBlank val name: String,
    @field:NotBlank val nickname: String,
    val birthday: LocalDate
)

data class UpdateUserInfoResult(
    val userToken: String,
    val name: String,
    val nickname: String,
    val birthday: LocalDate
)
