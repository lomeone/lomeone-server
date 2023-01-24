package io.github.comstering.usecase.user

import io.github.comstering.domain.user.entity.AccountType
import io.github.comstering.domain.user.entity.User
import io.github.comstering.domain.user.repository.UserRepository
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
                birthday = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"))
            )

            val response = withContext(Dispatchers.IO) {
                updateUserInfoService.execute(request)
            }
            Then("유저 정보가 업데이트된다") {
                response.firebaseUserToken shouldBe request.firebaseUserToken
                response.name shouldBe request.name
                response.nickname shouldBe request.nickname
                response.birthday shouldBe request.birthday
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
                birthday = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul"))
            )
            Then("예외가 발생해서 유저 정보를 업데이트할 수 없다") {
                shouldThrow<Exception> {
                    withContext(Dispatchers.IO) {
                        updateUserInfoService.execute(request)
                    }
                }
            }
        }
    }
})
