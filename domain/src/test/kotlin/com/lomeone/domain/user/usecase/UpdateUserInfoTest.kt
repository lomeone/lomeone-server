package com.lomeone.domain.user.usecase

import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class UpdateUserInfoTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val updateUserInfo = UpdateUserInfo(userRepository)

    Given("유저가 존재하면") {
        every { userRepository.findByUserToken(any()) } returns User(
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now(),
        )
        When("유저 정보를 업데이트할 때") {
            val command = UpdateUserInfoServiceCommand(
                userToken = "user1234",
                name = "John",
                nickname = "Tomy",
                birthday = LocalDate.of(2000, 1, 1),
            )

            val result = withContext(Dispatchers.IO) {
                updateUserInfo.execute(command)
            }
            Then("유저 정보가 업데이트된다") {
                result.name shouldBe command.name
                result.nickname shouldBe command.nickname
                result.birthday shouldBe command.birthday
            }
        }
    }
    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null
        When("유저 정보를 업데이트할 때") {
            val command = UpdateUserInfoServiceCommand(
                userToken = "user1234",
                name = "John",
                nickname = "Tomy",
                birthday = LocalDate.of(2000, 1, 1),
            )
            Then("유저를 찾을 수 없다는 예외가 발생해서 유저 정보를 업데이트할 수 없다") {
                shouldThrow<Exception> {
                    withContext(Dispatchers.IO) {
                        updateUserInfo.execute(command)
                    }
                }
            }
        }
    }
})
