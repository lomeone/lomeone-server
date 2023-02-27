package com.lomeone.user.usecase

import com.lomeone.domain.user.entity.AccountType
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import com.lomeone.domain.user.usecase.UpdateUserInfo
import com.lomeone.domain.user.usecase.UpdateUserInfoServiceRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZonedDateTime

class UpdateUserInfoTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val updateUserInfo = UpdateUserInfo(userRepository)

    Given("유저가 존재하면") {
        every { userRepository.findByFirebaseUserToken(any()) } returns User(
            firebaseUserToken = "user1234",
            name = "name",
            nickname = "nickname",
            email = "email@gmail.com",
            birthday = ZonedDateTime.now(),
            photoUrl = "https://photo.com",
            accountType = AccountType.GOOGLE
        )
        When("유저 정보를 업데이트할 때") {
            val request = UpdateUserInfoServiceRequest(
                firebaseUserToken = "user1234",
                name = "John",
                nickname = "Tomy",
                birthday = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                photoUrl = "https://photo.com",
            )

            val response = withContext(Dispatchers.IO) {
                updateUserInfo.execute(request)
            }
            Then("유저 정보가 업데이트된다") {
                response.firebaseUserToken shouldBe request.firebaseUserToken
                response.name shouldBe request.name
                response.nickname shouldBe request.nickname
                response.birthday shouldBe request.birthday
                response.photoUrl shouldBe request.photoUrl
            }
        }
    }
    Given("유저가 존재하지 않으면") {
        every { userRepository.findByFirebaseUserToken(any()) } returns null
        When("유저 정보를 업데이트할 때") {
            val request = UpdateUserInfoServiceRequest(
                firebaseUserToken = "user1234",
                name = "John",
                nickname = "Tomy",
                birthday = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul")),
                photoUrl = "https://photo.com",
            )
            Then("예외가 발생해서 유저 정보를 업데이트할 수 없다") {
                shouldThrow<Exception> {
                    withContext(Dispatchers.IO) {
                        updateUserInfo.execute(request)
                    }
                }
            }
        }
    }
})
