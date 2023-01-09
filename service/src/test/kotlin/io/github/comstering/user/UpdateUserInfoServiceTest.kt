package io.github.comstering.user

import io.github.comstering.user.entity.AccountType
import io.github.comstering.user.entity.User
import io.github.comstering.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZonedDateTime

class UpdateUserInfoServiceTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val updateUserInfoService = UpdateUserInfoService(userRepository)

    Given("유저가 존재하면") {
        every { userRepository.findByFirebaseUserToken(any()) } returns User(
            firebaseUserToken = "user1234",
            name = "name",
            nickname = "nickname",
            email = "email@gmail.com",
            birthday = ZonedDateTime.now(),
            accountType = AccountType.GOOGLE
        )
        When("유저 정보를 업데이트할 때") {
            val request = UpdateUserInfoServiceRequest(
                firebaseUserToken = "user1234",
                name = "John",
                nickname = "Tomy",
                email = "google@gmail.com",
                birthday = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"))
            )

            val result = withContext(Dispatchers.IO) {
                updateUserInfoService.execute(request)
            }
            Then("유저 정보가 업데이트된다") {
                val (firebaseUserToken, name, nickname, email, birthday) = result
                firebaseUserToken shouldBe request.firebaseUserToken
                name shouldBe request.name
                nickname shouldBe request.nickname
                email shouldBe request.email
                birthday shouldBe request.birthday
            }
        }
    }
})
