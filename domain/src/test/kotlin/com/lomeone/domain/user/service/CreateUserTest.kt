package com.lomeone.domain.user.service

import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate

class CreateUserTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val createUser = CreateUser(userRepository)

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null
        When("유저를 생성할 때") {
            val command = CreateUserServiceCommand(
                name = "name",
                nickname = "nickname",
                email = "email@gmail.com",
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
            )

            every { userRepository.save(any()) } returns User(
                name = "name",
                nickname = "nickname",
                email = Email("email@gmail.com"),
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
            )

            val result = withContext(Dispatchers.IO) {
                createUser.execute(command)
            }
            Then("유저가 생성된다") {
                result.id shouldBe 0L
                result.name shouldBe command.name
                result.nickname shouldBe command.nickname
                result.email shouldBe command.email
                result.phoneNumber shouldBe command.phoneNumber
                result.birthday shouldBe command.birthday
            }
        }
    }
})
