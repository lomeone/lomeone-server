package com.lomeone.user.service

import com.lomeone.user.entity.User
import com.lomeone.user.exception.UserNotFoundException
import com.lomeone.user.repository.UserRepository
import jakarta.validation.constraints.NotBlank
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUserByUserToken(
    private val userRepository: com.lomeone.user.repository.UserRepository
) {
    @Transactional(readOnly = true)
    fun execute(request: com.lomeone.user.service.GetUserByUserTokenQuery): com.lomeone.user.service.GetUserByUserTokenResult {
        val user = getUser(request.userToken)
        return _root_ide_package_.com.lomeone.user.service.GetUserByUserTokenResult(user)
    }

    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken) ?: throw _root_ide_package_.com.lomeone.user.exception.UserNotFoundException(
            mapOf("user_token" to userToken)
        )
}

data class GetUserByUserTokenQuery(@field:NotBlank val userToken: String)

data class GetUserByUserTokenResult(val user: com.lomeone.user.entity.User)
