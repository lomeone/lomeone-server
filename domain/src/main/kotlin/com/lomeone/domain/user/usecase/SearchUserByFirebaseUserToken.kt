package com.lomeone.domain.user.usecase

import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SearchUserByFirebaseUserToken(
    private val userRepository: UserRepository
) {
    @Transactional(readOnly = true)
    fun execute(request: SearchUserByFirebaseUserTokenRequest): SearchUserByFirebaseUserTokenResponse {
        val user = searchUserByFirebaseUserToken(request.firebaseUserToken)
        return SearchUserByFirebaseUserTokenResponse(user)
    }

    private fun searchUserByFirebaseUserToken(firebaseUserToken: String) =
        userRepository.findByFirebaseUserToken(firebaseUserToken) ?: throw Exception("User not found")
}

data class SearchUserByFirebaseUserTokenRequest(val firebaseUserToken: String)

data class SearchUserByFirebaseUserTokenResponse(val user: User)
