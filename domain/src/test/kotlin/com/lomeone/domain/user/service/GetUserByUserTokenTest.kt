package com.lomeone.domain.user.service

import com.lomeone.common.entity.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class GetUserByUserTokenTest : BehaviorSpec({
    val userRepository: com.lomeone.user.repository.UserRepository = mockk()
    val getUserByUserTokenService = _root_ide_package_.com.lomeone.user.service.GetUserByUserToken(userRepository)

    Given("userToken을 가지고 있는 유저가 존재하면") {
        val userTokenInput = "user1234"
        every { userRepository.findByUserToken(userTokenInput) } returns _root_ide_package_.com.lomeone.user.entity.User(
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.of(2000, 1, 1)
        )

        When("유저를 검색할 때") {
            val query = _root_ide_package_.com.lomeone.user.service.GetUserByUserTokenQuery(userTokenInput)
            val result = getUserByUserTokenService.execute(query)

            Then("유저가 검색된다") {
                result.user.name shouldBe "name"
                result.user.nickname shouldBe "nickname"
                result.user.email shouldBe Email("email@gmail.com")
                result.user.phoneNumber shouldBe "+821012345678"
                result.user.birthday shouldBe LocalDate.of(2000, 1, 1)
            }
        }
    }

    Given("userToken을 가지고 있는 유저가 존재하지 않으면") {
        val userTokenInput = "user1234"
        every { userRepository.findByUserToken(userTokenInput) } returns null

        When("유저를 검색할 때") {
            val query = _root_ide_package_.com.lomeone.user.service.GetUserByUserTokenQuery(userTokenInput)

            Then("유저를 찾을 수 없다는 예외가 발생해서 유저를 검색할 수 없다") {
                shouldThrow<com.lomeone.user.exception.UserNotFoundException> {
                    getUserByUserTokenService.execute(query)
                }
            }
        }
    }
})
