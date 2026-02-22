package com.lomeone.domain.user.service

import com.lomeone.common.entity.Email
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class DeletionRequestUserTest : BehaviorSpec({
    val userRepository: com.lomeone.user.repository.UserRepository = mockk()
    val deletionRequestRepository: com.lomeone.user.repository.DeletionRequestRepository = mockk()
    val deletionRequestUserService = _root_ide_package_.com.lomeone.user.service.RequestUserDeletion(
        userRepository = userRepository,
        deletionRequestRepository = deletionRequestRepository
    )

    beforeTest {
        every { deletionRequestRepository.save(any()) } returns _root_ide_package_.com.lomeone.user.entity.DeletionRequest(
            userToken = "user1234",
            reason = "탈퇴 요청"
        )
    }

    Given("삭제를 요청하지 않은 유저라면") {
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), _root_ide_package_.com.lomeone.user.entity.DeletionStatus.REQUEST) } returns null
        every { userRepository.findByUserToken(any()) } returns _root_ide_package_.com.lomeone.user.entity.User(
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now()
        )

        When("삭제 요청할 때") {
            val command = _root_ide_package_.com.lomeone.user.service.DeletionRequestUserCommand(
                userToken = "user1234",
                reason = "탈퇴 요청"
            )

            val result = deletionRequestUserService.execute(command)

            Then("삭제 요청이 처리된다.") {
                result.userToken shouldBe command.userToken
                result.deletionCompletedAt shouldBe LocalDate.now()
            }
        }
    }

    Given("이미 삭제 요청이 있으면") {
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), _root_ide_package_.com.lomeone.user.entity.DeletionStatus.REQUEST) } returns mockk()

        When("삭제 요청할 때") {
            val command = _root_ide_package_.com.lomeone.user.service.DeletionRequestUserCommand(
                userToken = "user1234",
                reason = "탈퇴 요청"
            )

            Then("이미 삭제 요청이 있다는 예외가 발생해서 삭제 요청을 할 수 없다") {
                shouldThrow<com.lomeone.user.exception.DeletionRequestAlreadyExistsException> {
                    deletionRequestUserService.execute(command)
                }
            }
        }
    }

    Given("유저가 존재하지 않으면") {
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), _root_ide_package_.com.lomeone.user.entity.DeletionStatus.REQUEST) } returns null
        every { userRepository.findByUserToken(any()) } returns null

        When("삭제 요청할 때") {
            val command = _root_ide_package_.com.lomeone.user.service.DeletionRequestUserCommand(
                userToken = "user1234",
                reason = "탈퇴 요청"
            )

            Then("유저를 찾을 수 없다는 예외가 발생해서 삭제 요청을 할 수 없다") {
                shouldThrow<com.lomeone.user.exception.UserNotFoundException> {
                    deletionRequestUserService.execute(command)
                }
            }
        }
    }
})
