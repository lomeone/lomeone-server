package com.lomeone.domain.authentication.service

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.security.authentication.AuthenticationManager

class AuthenticateByEmailTest : BehaviorSpec({
    val authenticationManager: AuthenticationManager = mockk()
    val jwtTokenProvider: JwtTokenProvider = mockk()
    val authenticateByEmail = AuthenticateByEmail(authenticationManager, jwtTokenProvider)

    Given("이메일 인증정보가 있으면") {
        val emailInput = "test@gmail.com"
        val passwordInput = "testPassword1324@"

        When("이메일 인증할 때") {
            val command = AuthenticateByEmailCommand(
                email = emailInput,
                password = passwordInput
            )

            every { authenticationManager.authenticate(any()) } returns mockk()
            every { jwtTokenProvider.issueToken(any()) } returns TokenInfo(
                accessToken = "accessToken",
                refreshToken = "refreshToken",
                expiresIn = 18000
            )

            Then("인증이 완료된다") {
                val result = authenticateByEmail.authenticate(command)

                result.accessToken shouldBe "accessToken"
                result.refreshToken shouldBe "refreshToken"
                result.expiresIn shouldBe 18000
            }
        }
    }
})
