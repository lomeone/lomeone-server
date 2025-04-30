package com.lomeone.domain.authentication.service

import com.lomeone.domain.authentication.entity.Authentication
import com.lomeone.domain.authentication.exception.AuthenticationNotFoundException
import com.lomeone.domain.authentication.repository.AuthenticationRepository
import com.lomeone.domain.realm.entity.Realm
import com.lomeone.domain.realm.exception.RealmNotFoundException
import com.lomeone.domain.realm.repository.RealmRepository
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.exception.UserNotFoundException
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class AssociateAuthenticationToUserTest : BehaviorSpec({
    val authenticationRepository: AuthenticationRepository = mockk()
    val realmRepository: RealmRepository = mockk()
    val userRepository: UserRepository = mockk()
    val associateAuthenticationToUser = AssociateAuthenticationToUser(
        authenticationRepository,
        realmRepository,
        userRepository
    )

    beforeTest {
        val mockAuthentication: Authentication = mockk()
        every { mockAuthentication.user } returns null
    }

    Given("유저, Realm, 인증이 존재하면") {
        val mockAuthentication: Authentication = mockk()
        val mockUser: User = mockk()
        val mockRealm: Realm = mockk()
        every { mockUser.userToken } returns "user-token"
        every { mockRealm.code } returns "code1234"
        every { mockAuthentication.uid } returns "uid1234"
        every { mockAuthentication.associateUser(any()) } returns Unit

        every { userRepository.findByUserToken(any()) } returns mockUser
        every { realmRepository.findByCode(any()) } returns mockRealm
        every { authenticationRepository.findByUidAndRealm(any(), any()) } returns mockAuthentication

        When("인증에 유저를 연결할 때") {
            val command = AssociateAuthenticationToUserCommand(
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
        val mockUser: User = mockk()
        val mockRealm: Realm = mockk()
        every { mockUser.userToken } returns "user-token"
        every { mockRealm.code } returns "code1234"

        every { userRepository.findByUserToken(any()) } returns mockUser
        every { realmRepository.findByCode(any()) } returns mockRealm
        every { authenticationRepository.findByUidAndRealm(any(), any()) } returns null

        When("인증에 유저를 연결할 때") {
            val command = AssociateAuthenticationToUserCommand(
                "user-token",
                "uid1234",
                "code1234"
            )

            Then("인증이 존재하지 않는 예외가 발생해서 인증에 유저를 연결할 수 없다") {
                shouldThrow<AuthenticationNotFoundException> {
                    associateAuthenticationToUser.execute(command)
                }
            }
        }
    }

    Given("Realm이 존재하지 않으면") {
        val mockUser: User = mockk()
        every { mockUser.userToken } returns "user-token"
        every { realmRepository.findByCode(any()) } returns null

        every { userRepository.findByUserToken(any()) } returns mockUser

        When("인증에 유저를 연결할 때") {
            val command = AssociateAuthenticationToUserCommand(
                "user-token",
                "uid1234",
                "code1234"
            )

            Then("Realm이 존재하지 않는 예외가 발생해서 인증에 유저를 연결할 수 없다") {
                shouldThrow<RealmNotFoundException> {
                    associateAuthenticationToUser.execute(command)
                }
            }
        }
    }

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null

        When("인증에 유저를 연결할 때") {
            val command = AssociateAuthenticationToUserCommand(
                "user-token",
                "uid1234",
                "code1234"
            )

            Then("유저가 존재하지 않는 예외가 발생해서 인증에 유저를 연결할 수 없다") {
                shouldThrow<UserNotFoundException> {
                    associateAuthenticationToUser.execute(command)
                }
            }
        }
    }
})
