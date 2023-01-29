package io.github.comstering.domain.user.usecase

import io.github.comstering.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZonedDateTime

@Service
class UpdateUserInfo(
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(request: UpdateUserInfoServiceRequest): UpdateUserInfoServiceResponse {
        val (firebaseUserToken, name, nickname, birthday, photoUrl) = request

        val user = getUser(firebaseUserToken)

        user.updateUserInfo(name, nickname, birthday, photoUrl)

        return UpdateUserInfoServiceResponse(
            firebaseUserToken = user.firebaseUserToken,
            name = user.name,
            nickname = user.nickname,
            photoUrl = user.photoUrl,
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
    val birthday: ZonedDateTime,
    val photoUrl: String
)

data class UpdateUserInfoServiceResponse(
    val firebaseUserToken: String,
    val name: String,
    val nickname: String,
    val birthday: ZonedDateTime,
    val photoUrl: String
)
