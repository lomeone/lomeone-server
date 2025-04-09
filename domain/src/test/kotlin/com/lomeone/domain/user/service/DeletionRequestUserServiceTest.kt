package com.lomeone.domain.user.service

import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.DeletionRequest
import com.lomeone.domain.user.entity.DeletionStatus
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.exception.DeletionRequestAlreadyExistsException
import com.lomeone.domain.user.exception.UserNotFoundException
import com.lomeone.domain.user.repository.DeletionRequestRepository
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class DeletionRequestUserServiceTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val deletionRequestRepository: DeletionRequestRepository = mockk()
    val deletionRequestUserService = DeletionRequestUserService(
        userRepository = userRepository,
        deletionRequestRepository = deletionRequestRepository
    )

    beforeTest {
        every { deletionRequestRepository.save(any()) } returns DeletionRequest(
            userToken = "user1234",
            reason = "탈퇴 요청"
        )
    }

    Given("삭제를 요청하지 않은 유저라면") {
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), DeletionStatus.REQUEST) } returns null
        every { userRepository.findByUserToken(any()) } returns User(
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now()
        )

        When("삭제 요청할 때") {
            val command = DeletionRequestUserCommand(
                userToken = "user1234",
                reason = "탈퇴 요청"
            )

            val result = deletionRequestUserService.deletionRequestUser(command)

            Then("삭제 요청이 처리된다.") {
                result.userToken shouldBe command.userToken
                result.deletionCompletedAt shouldBe LocalDate.now()
            }
        }
    }

    Given("이미 삭제 요청이 있으면") {
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), DeletionStatus.REQUEST) } returns mockk()

        When("삭제 요청할 때") {
            val command = DeletionRequestUserCommand(
                userToken = "user1234",
                reason = "탈퇴 요청"
            )

            Then("이미 삭제 요청이 있다는 예외가 발생해서 삭제 요청을 할 수 없다") {
                shouldThrow<DeletionRequestAlreadyExistsException> {
                    deletionRequestUserService.deletionRequestUser(command)
                }
            }
        }
    }

    Given("유저가 존재하지 않으면") {
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), DeletionStatus.REQUEST) } returns null
        every { userRepository.findByUserToken(any()) } returns null

        When("삭제 요청할 때") {
            val command = DeletionRequestUserCommand(
                userToken = "user1234",
                reason = "탈퇴 요청"
            )

            Then("유저를 찾을 수 없다는 예외가 발생해서 삭제 요청을 할 수 없다") {
                shouldThrow<UserNotFoundException> {
                    deletionRequestUserService.deletionRequestUser(command)
                }
            }
        }
    }
})
