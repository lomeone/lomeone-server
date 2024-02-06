package com.lomeone.domain.user.service

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Authentication
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

class CreateUserServiceTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val createUserService = CreateUserService(userRepository)

    val authenticationInput = Authentication(
        email = Email("test@gmail.com"),
        password = "testPassword1324@",
        provider = AuthProvider.EMAIL
    )

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null
        When("유저를 생성할 때") {
            val command = CreateUserCommand(
                name = "name",
                nickname = "nickname",
                email = "email@gmail.com",
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
                authentication = authenticationInput
            )

            every { userRepository.save(any()) } returns User(
                name = "name",
                nickname = "nickname",
                email = Email("email@gmail.com"),
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
            )

            val result = withContext(Dispatchers.IO) {
                createUserService.createUser(command)
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
