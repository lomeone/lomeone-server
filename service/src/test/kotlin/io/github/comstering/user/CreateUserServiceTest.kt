package io.github.comstering.user

import io.github.comstering.user.entity.AccountType
import io.github.comstering.user.entity.Email
import io.github.comstering.user.entity.User
import io.github.comstering.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
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

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByFirebaseUserToken(any()) } returns null
        When("유저를 생성할 때") {
            val request = CreateUserServiceRequest(
                firebaseUserToken = "user1234",
                name = "name",
                nickname = "nickname",
                email = "test@gmail.com",
                birthday = LocalDate.of(2000, 1, 1),
                accountType = "GOOGLE"
            )

            every { userRepository.save(any()) } returns User(
                firebaseUserToken = "user1234",
                name = "name",
                nickname = "nickname",
                email = Email("test@gmail.com"),
                birthday = LocalDate.of(2000, 1, 1),
                accountType = AccountType.GOOGLE
            )

            val response = withContext(Dispatchers.IO) {
                createUserService.execute(request)
            }
            Then("유저가 생성된다") {
                request.firebaseUserToken shouldBe response.firebaseUserToken
                request.name shouldBe response.name
                request.nickname shouldBe response.nickname
                request.email shouldBe response.email
                request.birthday shouldBe response.birthday
                request.accountType shouldBe response.accountType
            }
        }
    }

    Given("유저가 이미 존재하면") {
        every { userRepository.findByFirebaseUserToken(any()) } returns mockk<User>()
        When("유저를 생성할 때") {
            val request = CreateUserServiceRequest(
                firebaseUserToken = "user1234",
                name = "name",
                nickname = "nickname",
                email = "test@gmail.com",
                birthday = LocalDate.of(2000, 1, 1),
                accountType = "GOOGLE"
            )
            Then("유저가 이미 존재한다는 예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<Exception> {
                    createUserService.execute(request)
                }
            }
        }
    }
})
