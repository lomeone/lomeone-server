package com.lomeone.user.service

import com.lomeone.common.entity.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class RestoreUserTest : BehaviorSpec({
    val userRepository: com.lomeone.user.repository.UserRepository = mockk()
    val deletionRequestRepository: com.lomeone.user.repository.DeletionRequestRepository = mockk()
    val restoreUserService =
        _root_ide_package_.com.lomeone.user.service.RestoreUser(userRepository, deletionRequestRepository)


    Given("삭제 요청을 했고 유저 정보에 있는 유저 토큰으로") {
        val defaultUser = _root_ide_package_.com.lomeone.user.entity.User(
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now()
        )
        val defaultDeletionRequest = _root_ide_package_.com.lomeone.user.entity.DeletionRequest(
            userToken = "user1234",
            reason = "delete"
        )

        every { userRepository.findByUserToken(any()) } returns defaultUser
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), _root_ide_package_.com.lomeone.user.entity.DeletionStatus.REQUEST) } returns defaultDeletionRequest

        When("복구할 때") {
            val command = _root_ide_package_.com.lomeone.user.service.RestoreUserCommand(defaultUser.userToken)
            val result = restoreUserService.execute(command)

            Then("복구된다") {
                result.userToken shouldBe defaultUser.userToken
            }
        }
    }

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null

        When("복구할 때") {
            val command = _root_ide_package_.com.lomeone.user.service.RestoreUserCommand("user1234")

            Then("유저가 없다는 예외가 발생해서 복구할 수 없다") {
                shouldThrow<com.lomeone.user.exception.UserNotFoundException> {
                    restoreUserService.execute(command)
                }
            }
        }
    }

    Given("삭제 요청을 하지 않았으면") {
        val defaultUser = _root_ide_package_.com.lomeone.user.entity.User(
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now()
        )

        every { userRepository.findByUserToken(any()) } returns defaultUser
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), _root_ide_package_.com.lomeone.user.entity.DeletionStatus.REQUEST) } returns null

        When("복구할 때") {
            val command = _root_ide_package_.com.lomeone.user.service.RestoreUserCommand("user1234")

            Then("삭제 요청을 찾을 수 없다는 예외가 발생해서 복구할 수 없다") {
                shouldThrow<com.lomeone.user.exception.DeletionRequestNotFoundException> {
                    restoreUserService.execute(command)
                }
            }
        }
    }
})
