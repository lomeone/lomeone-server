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

class CreateUserTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val createUser = CreateUser(userRepository)

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null
        When("유저를 생성할 때") {
            val command = CreateUserServiceCommand(
                userToken = "user1234",
                name = "name",
                nickname = "nickname",
                email = "email@gmail.com",
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
                photoUrl = "https://photo.com"
            )

            every { userRepository.save(any()) } returns User(
                userToken = "user1234",
                name = "name",
                nickname = "nickname",
                email = Email("email@gmail.com"),
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
                photoUrl = "https://photo.com"
            )

            val result = withContext(Dispatchers.IO) {
                createUser.execute(command)
            }
            Then("유저가 생성된다") {
                result.id shouldBe 0L
                result.userToken shouldBe command.userToken
                result.name shouldBe command.name
                result.nickname shouldBe command.nickname
                result.email shouldBe command.email
                result.phoneNumber shouldBe command.phoneNumber
                result.birthday shouldBe command.birthday
                result.photoUrl shouldBe command.photoUrl
            }
        }
    }

    Given("유저가 이미 존재하면") {
        every { userRepository.findByUserToken(any()) } returns mockk<User>()
        When("유저를 생성할 때") {
            val command = CreateUserServiceCommand(
                userToken = "user1234",
                name = "name",
                nickname = "nickname",
                email = "email@gmail.com",
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
                photoUrl = "https://photo.com"
            )
            Then("유저가 이미 존재한다는 예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    createUser.execute(command)
                }
            }
        }
    }
})
