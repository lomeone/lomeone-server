package com.lomeone.authentication.service

import com.lomeone.authentication.entity.Authentication
import com.lomeone.authentication.exception.AuthenticationNotFoundException
import com.lomeone.authentication.repository.AuthenticationRepository
import com.lomeone.authentication.entity.Realm
import com.lomeone.authentication.exception.RealmNotFoundException
import com.lomeone.authentication.repository.RealmRepository
import com.lomeone.user.entity.User
import com.lomeone.user.exception.UserNotFoundException
import com.lomeone.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AssociateAuthenticationToUserTest : BehaviorSpec({
    val authenticationRepository: com.lomeone.authentication.repository.AuthenticationRepository = mockk()
    val realmRepository: com.lomeone.authentication.repository.RealmRepository = mockk()
    val userRepository: com.lomeone.user.repository.UserRepository = mockk()
    val associateAuthenticationToUser =
        _root_ide_package_.com.lomeone.authentication.service.AssociateAuthenticationToUser(
            authenticationRepository,
            realmRepository,
            userRepository
        )

    beforeTest {
        val mockAuthentication: com.lomeone.authentication.entity.Authentication = mockk()
        every { mockAuthentication.user } returns null
    }

    Given("유저, Realm, 인증이 존재하면") {
        val mockAuthentication: com.lomeone.authentication.entity.Authentication = mockk()
        val mockUser: com.lomeone.user.entity.User = mockk()
        val mockRealm: com.lomeone.authentication.entity.Realm = mockk()
        every { mockUser.userToken } returns "user-token"
        every { mockRealm.code } returns "code1234"
        every { mockAuthentication.uid } returns "uid1234"
        every { mockAuthentication.associateUser(any()) } returns Unit

        every { userRepository.findByUserToken(any()) } returns mockUser
        every { realmRepository.findByCode(any()) } returns mockRealm
        every { authenticationRepository.findByUidAndRealm(any(), any()) } returns mockAuthentication

        When("인증에 유저를 연결할 때") {
            val command = _root_ide_package_.com.lomeone.authentication.service.AssociateAuthenticationToUserCommand(
                "user-token",
                "uid1234",
                "code1234"
            )

            val result = associateAuthenticationToUser.execute(command)

            Then("인증에 유저가 연결된다") {
                result.userToken shouldBe "user-token"
                result.uid shouldBe "uid1234"
                result.realmCode shouldBe "code1234"
            }
        }
    }

    Given("인증이 존재하지 않으면") {
        val mockUser: com.lomeone.user.entity.User = mockk()
        val mockRealm: com.lomeone.authentication.entity.Realm = mockk()
        every { mockUser.userToken } returns "user-token"
        every { mockRealm.code } returns "code1234"

        every { userRepository.findByUserToken(any()) } returns mockUser
        every { realmRepository.findByCode(any()) } returns mockRealm
        every { authenticationRepository.findByUidAndRealm(any(), any()) } returns null

        When("인증에 유저를 연결할 때") {
            val command = _root_ide_package_.com.lomeone.authentication.service.AssociateAuthenticationToUserCommand(
                "user-token",
                "uid1234",
                "code1234"
            )

            Then("인증이 존재하지 않는 예외가 발생해서 인증에 유저를 연결할 수 없다") {
                shouldThrow<com.lomeone.authentication.exception.AuthenticationNotFoundException> {
                    associateAuthenticationToUser.execute(command)
                }
            }
        }
    }

    Given("Realm이 존재하지 않으면") {
        val mockUser: com.lomeone.user.entity.User = mockk()
        every { mockUser.userToken } returns "user-token"
        every { realmRepository.findByCode(any()) } returns null

        every { userRepository.findByUserToken(any()) } returns mockUser

        When("인증에 유저를 연결할 때") {
            val command = _root_ide_package_.com.lomeone.authentication.service.AssociateAuthenticationToUserCommand(
                "user-token",
                "uid1234",
                "code1234"
            )

            Then("Realm이 존재하지 않는 예외가 발생해서 인증에 유저를 연결할 수 없다") {
                shouldThrow<com.lomeone.authentication.exception.RealmNotFoundException> {
                    associateAuthenticationToUser.execute(command)
                }
            }
        }
    }

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null

        When("인증에 유저를 연결할 때") {
            val command = _root_ide_package_.com.lomeone.authentication.service.AssociateAuthenticationToUserCommand(
                "user-token",
                "uid1234",
                "code1234"
            )

            Then("유저가 존재하지 않는 예외가 발생해서 인증에 유저를 연결할 수 없다") {
                shouldThrow<com.lomeone.user.exception.UserNotFoundException> {
                    associateAuthenticationToUser.execute(command)
                }
            }
        }
    }
})
