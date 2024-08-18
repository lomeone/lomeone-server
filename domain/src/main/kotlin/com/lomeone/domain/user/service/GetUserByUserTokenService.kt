package com.lomeone.domain.user.service

import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.exception.UserNotFoundException
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetUserByUserTokenService(
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun getUserByUserToken(request: GetUserByUserTokenQuery): GetUserByUserTokenResult {
        val user = getUser(request.userToken)
        return GetUserByUserTokenResult(user)
    }

    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken) ?: throw UserNotFoundException(mapOf("user_token" to userToken))
}

data class GetUserByUserTokenQuery(val userToken: String)

data class GetUserByUserTokenResult(val user: User)
