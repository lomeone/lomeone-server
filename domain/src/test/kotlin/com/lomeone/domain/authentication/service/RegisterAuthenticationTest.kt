package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.AuthProvider
import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.exception.AuthenticationAlreadyExistsException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.realm.entity.Realm
import com.lomeone.domain.realm.repository.RealmRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

class RegisterAuthenticationTest : BehaviorSpec({
    val authenticationRepository: AuthenticationRepository = mockk()
    val realmRepository: RealmRepository = mockk()
    val bCryptPasswordEncoder: BCryptPasswordEncoder = mockk()
    val registerAuthenticationService = RegisterAuthentication(authenticationRepository, realmRepository, bCryptPasswordEncoder)

    val emailInput = "test@gmail.com"
    val uidInput = "testUid1234"
    val passwordInput = "testPassword1324@"
    val providerInput = AuthProvider.EMAIL
    val realmCodeInput = "testRealmCode"

    val mockRealm = mockk<Realm>()

    Given("realm이 없으면") {
        every { realmRepository.findByCode(any())} returns null

        When("인증정보를 생성할 때") {
            val command = RegisterAuthenticationCommand(
                email = emailInput,
                uid = uidInput,
                password = passwordInput,
                provider = providerInput,
                realmCode = realmCodeInput
            )

            Then("realm이 없다는 예외가 발생해서 인증정보 생성에 실패한다") {
                shouldThrow<IllegalArgumentException> {
                    registerAuthenticationService.execute(command)
                }
            }
        }
    }

    Given("중복된 인증정보가 없으면") {
        every { realmRepository.findByCode(any()) } returns mockRealm
        every { authenticationRepository.findByEmailAndProviderAndRealm(any(), any(), any()) } returns null
        every { authenticationRepository.findByUid(any()) } returns null
        every { bCryptPasswordEncoder.encode(any()) } returns "encodePassword1324@"

        When("비밀번호가 없는 인증정보를 생성할 때") {
            val command = RegisterAuthenticationCommand(
                email = emailInput,
                provider = AuthProvider.GOOGLE,
                realmCode = realmCodeInput,
            )

            every { authenticationRepository.save(any()) } returns Authentication(
                uid = uidInput,
                email = Email(emailInput),
                provider = AuthProvider.GOOGLE,
                realm = mockRealm
            )

            val result = registerAuthenticationService.execute(command)

            Then("비밀번호 인코딩을 하지 않고 인증정보가 생성된다") {
                verify(exactly = 0) { bCryptPasswordEncoder.encode(any()) }

                result.uid shouldBe uidInput
            }
        }

        When("비밀번호가 있는 인증정보를 생성할 때") {
            val command = RegisterAuthenticationCommand(
                email = emailInput,
                password = passwordInput,
                provider = providerInput,
                realmCode = realmCodeInput
            )

            every { authenticationRepository.save(any()) } returns Authentication(
                uid = uidInput,
                email = Email(emailInput),
                password = passwordInput,
                provider = providerInput,
                realm = mockRealm
            )

            val result = registerAuthenticationService.execute(command)

            Then("비밀번호 인코딩을 하고 인증정보가 생성된다") {
                verify { bCryptPasswordEncoder.encode(any()) }

                result.uid shouldBe uidInput
            }
        }
    }

    Given("같은 realm, 같은 provider에 동일한 이메일 인증정보가 있으면") {
        every { realmRepository.findByCode(any()) } returns mockRealm
        every { mockRealm.code } returns realmCodeInput
        every { authenticationRepository.findByEmailAndProviderAndRealm(emailInput, providerInput, mockRealm) } returns Authentication(
            uid = uidInput,
            email = Email(emailInput),
            password = passwordInput,
            provider = providerInput,
            realm = mockRealm
        )
        every { authenticationRepository.findByUid(uidInput) } returns null

        When("인증정보를 생성할 때") {
            val command = RegisterAuthenticationCommand(
                email = emailInput,
                uid = uidInput,
                password = passwordInput,
                provider = providerInput,
                realmCode = realmCodeInput
            )

            Then("중복된 인증정보가 있다는 예외가 발생해서 인증정보 생성에 실패한다") {
                shouldThrow<AuthenticationAlreadyExistsException> {
                    registerAuthenticationService.execute(command)
                }
            }
        }
    }

    Given("동일한 UID의 인증정보가 있으면") {
        every { realmRepository.findByCode(any()) } returns mockRealm
        every { authenticationRepository.findByUid(uidInput) } returns Authentication(
            uid = uidInput,
            email = Email(emailInput),
            password = passwordInput,
            provider = providerInput,
            realm = mockRealm
        )
        every { authenticationRepository.findByEmailAndProviderAndRealm(emailInput, providerInput, any()) } returns null

        When("인증정보를 생성할 때") {
            val command = RegisterAuthenticationCommand(
                email = emailInput,
                uid = uidInput,
                password = passwordInput,
                provider = providerInput,
                realmCode = realmCodeInput
            )

            Then("중복된 인증정보가 있다는 예외가 발생해서 인증정보 생성에 실패한다") {
                shouldThrow<AuthenticationAlreadyExistsException> {
                    registerAuthenticationService.execute(command)
                }
            }
        }
    }
})
