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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class CreateAuthenticationServiceTest : BehaviorSpec({
    val authenticationRepository: AuthenticationRepository = mockk()
    val bCryptPasswordEncoder: BCryptPasswordEncoder = mockk()
    val createAuthenticationService = CreateAuthenticationService(authenticationRepository, bCryptPasswordEncoder)

    val emailInput = "test@gmail.com"
    val uidInput = "testUid1234"
    val passwordInput = "testPassword1324@"
    val providerInput = AuthProvider.EMAIL

    Given("중복된 인증정보가 없으면") {
        every { authenticationRepository.findByEmailAndProvider(any(), any()) } returns null
        every { authenticationRepository.findByUid(any()) } returns null
        every { bCryptPasswordEncoder.encode(any()) } returns "encodePassword1324@"

        When("인증정보를 생성할 때") {
            val command = CreateAuthenticationCommand(
                email = emailInput,
                uid = uidInput,
                password = passwordInput,
                provider = providerInput,
                user = mockk()
            )

            every { authenticationRepository.save(any()) } returns Authentication(
                uid = uidInput,
                email = Email(emailInput),
                password = passwordInput,
                provider = providerInput,
                user = mockk()
            )

            val result = withContext(Dispatchers.IO) {
                createAuthenticationService.createAuthentication(command)
            }
            Then("인증정보가 생성된다") {
                result.uid shouldBe uidInput
            }
        }
    }

    Given("같은 provider에 동이한 이메일 인증정보가 있으면") {
        every { authenticationRepository.findByEmailAndProvider(emailInput, providerInput) } returns Authentication(
            uid = uidInput,
            email = Email(emailInput),
            password = passwordInput,
            provider = providerInput,
            user = mockk()
        )
        When("인증정보를 생성할 때") {
            val command = CreateAuthenticationCommand(
                email = emailInput,
                uid = uidInput,
                password = passwordInput,
                provider = providerInput,
                user = mockk()
            )
            Then("중복된 인증정보가 있다는 예외가 발생해서 인증정보 생성에 실패한다") {
                shouldThrow<Exception> {
                    createAuthenticationService.createAuthentication(command)
                }
            }
        }
    }

    Given("동일한 UID의 인증정보가 있으면") {
        every { authenticationRepository.findByUid(uidInput) } returns Authentication(
            uid = uidInput,
            email = Email(emailInput),
            password = passwordInput,
            provider = providerInput,
            user = mockk()
        )
        When("인증정보를 생성할 때") {
            val command = CreateAuthenticationCommand(
                email = emailInput,
                uid = uidInput,
                password = passwordInput,
                provider = providerInput,
                user = mockk()
            )
            Then("중복된 인증정보가 있다는 예외가 발생해서 인증정보 생성에 실패한다") {
                shouldThrow<Exception> {
                    createAuthenticationService.createAuthentication(command)
                }
            }
        }
    }
})
