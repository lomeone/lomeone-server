package io.github.comstering.usecase.user

import io.github.comstering.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class UpdateUserInfoService(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(request: UpdateUserInfoServiceRequest): UpdateUserInfoServiceResponse {
        val (firebaseUserToken, name, nickname, birthday) = request

        val user = getUser(firebaseUserToken)

        user.updateUserInfo(name, nickname, birthday)

        return UpdateUserInfoServiceResponse(
            firebaseUserToken = user.firebaseUserToken,
            name = user.name,
            nickname = user.nickname,
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
    val birthday: ZonedDateTime
)

data class UpdateUserInfoServiceResponse(
    val firebaseUserToken: String,
    val name: String,
    val nickname: String,
    val birthday: ZonedDateTime
)
