package io.github.comstering.user

import io.github.comstering.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class UpdateUserInfoService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(request: UpdateUserInfoServiceRequest): UpdateUserInfoServiceResponse {
        val (firebaseUserToken, name, nickname, email, birthday) = request

        val user = getUser(firebaseUserToken)

        user.updateUserInfo(name, nickname, email, birthday)

        return UpdateUserInfoServiceResponse(
            firebaseUserToken = user.firebaseUserToken,
            name = user.name,
            nickname = user.nickname,
            email = user.email.value,
            birthday = user.birthday
        )
    }

    private fun getUser(firebaseUserToken: String) = userRepository.findByFirebaseUserToken(firebaseUserToken)
        ?: throw Exception("User not found")
}

data class UpdateUserInfoServiceRequest(
    val firebaseUserToken: String,
    val name: String,
    val nickname: String,
    val email: String,
    val birthday: ZonedDateTime
)

data class UpdateUserInfoServiceResponse(
    val firebaseUserToken: String,
    val name: String,
    val nickname: String,
    val email: String,
    val birthday: ZonedDateTime
)
