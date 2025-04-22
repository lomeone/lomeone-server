package com.lomeone.domain.user.service

import com.lomeone.domain.authentication.service.AssociateAuthenticationToUser
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.exception.UserEmailAlreadyExistsException
import com.lomeone.domain.user.exception.UserPhoneNumberAlreadyExistsException
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class CreateUserTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val associateAuthenticationToUser: AssociateAuthenticationToUser = mockk()
    val createUser = CreateUser(userRepository, associateAuthenticationToUser)

    beforeTest {
        val mockUser: User = mockk()
        every { mockUser.userToken } returns "user-token"
        every { userRepository.save(any()) } returns mockUser
    }

    Given("동일한 유저 이메일과 핸드폰 번호를 가지고 있는 유저가 없으면") {
        every { userRepository.findByEmail(any()) } returns null
        every { userRepository.findByPhoneNumber(any()) } returns null
        every { associateAuthenticationToUser.execute(any()) } returns mockk()

        When("유저를 생성할 때") {
            val command = CreateUserCommand(
                name = "name",
                nickname = "nickname",
                email = "email@gmail.com",
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
                authenticationUid = "uid1234",
                realmCode = "realmCode"
            )

            val result = createUser.execute(command)

            Then("유저가 생성된다") {
                result.userToken shouldBe "user-token"
            }
        }
    }

    Given("동일한 이메일을 가진 유저가 있으면") {
        every { userRepository.findByEmail(any()) } returns mockk()
        every { userRepository.findByPhoneNumber(any()) } returns null
        When("유저를 생성할 때") {
            val command = CreateUserCommand(
                name = "name",
                nickname = "nickname",
                email = "email@gmail.com",
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
                authenticationUid = "uid1234",
                realmCode = "realmCode"
            )

            Then("유저가 이미 존재한다는 예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<UserEmailAlreadyExistsException> {
                    createUser.execute(command)
                }
            }
        }
    }

    Given("동일한 핸드폰번호를 가진 유저가 있으면") {
        every { userRepository.findByEmail(any()) } returns null
        every { userRepository.findByPhoneNumber(any()) } returns mockk()

        When("유저를 생성할 때") {
            val command = CreateUserCommand(
                name = "name",
                nickname = "nickname",
                email = "email@gmail.com",
                phoneNumber = "+821012345678",
                birthday = LocalDate.of(2000, 1, 1),
                authenticationUid = "uid1234",
                realmCode = "realmCode"
            )

            Then("유저가 이미 존재한다는 예외가 발생해서 유저를 생성할 수 없다") {
                shouldThrow<UserPhoneNumberAlreadyExistsException> {
                    createUser.execute(command)
                }
            }
        }
    }
})
