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

class GetUserByUserTokenTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val getUserByUserToken = GetUserByUserToken(userRepository)

    Given("userToken을 가지고 있는 유저가 존재하면") {
        val userTokenInput = "user1234"
        every { userRepository.findByUserToken(userTokenInput) } returns User(
            userToken = userTokenInput,
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.of(2000, 1, 1),
            photoUrl = "https://photo.com"
        )
        When("유저를 검색할 때") {
            val result = withContext(Dispatchers.IO) {
                getUserByUserToken.execute(GetUserByUserTokenQuery(userTokenInput))
            }
            Then("유저가 검색된다") {
                result.user.userToken shouldBe userTokenInput
            }
        }
    }
    Given("userToken을 가지고 있는 유저가 존재하지 않으면") {
        val userTokenInput = "user1234"
        every { userRepository.findByUserToken(userTokenInput) } returns null
        When("유저를 검색할 때") {
            Then("유저를 찾을 수 없다는 예외가 발생해서 유저를 검색할 수 없다") {
                shouldThrow<Exception> {
                    getUserByUserToken.execute(GetUserByUserTokenQuery(userTokenInput))
                }
            }
        }
    }
})
