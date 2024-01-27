package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.common.entity.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreateAuthenticationServiceTest : BehaviorSpec({
    val authenticationRepository: AuthenticationRepository = mockk()
    val createAuthenticationService = CreateAuthenticationService(authenticationRepository)

    val emailInput = "test@gmail.com"
    val uidInput = "testUid1234"
    val passwordInput = "testPassword1324@"
    val providerInput = AuthProvider.EMAIL

    Given("중복된 인증정보가 없으면") {
        every { authenticationRepository.findByEmailAndProvider(any(), any()) } returns null
        every { authenticationRepository.findByUid(any()) } returns null
        When("인증정보를 생성할 때") {
            val command = CreateAccountCommand(
                email = emailInput,
                uid = uidInput,
                password = passwordInput,
                provider = providerInput
            )

            every { authenticationRepository.save(any()) } returns Authentication(
                uid = uidInput,
                email = Email(emailInput),
                password = passwordInput,
                provider = providerInput
            )

            val result = withContext(Dispatchers.IO) {
                createAuthenticationService.createAuthentication(command)
            }
            Then("인증정보가 생성된다") {
                result.uid shouldBe uidInput
            }
        }
    }
})
