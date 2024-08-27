package com.lomeone.domain.user.service

import com.lomeone.domain.common.entity.Email
import com.lomeone.domain.user.entity.DeletionRequest
import com.lomeone.domain.user.entity.DeletionStatus
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.exception.DeletionRequestNotFoundException
import com.lomeone.domain.user.exception.UserNotFoundException
import com.lomeone.domain.user.repository.DeletionRequestRepository
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.time.LocalDate

class RestoreUserServiceTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val deletionRequestRepository: DeletionRequestRepository = mockk()
    val restoreUserService = RestoreUserService(userRepository, deletionRequestRepository)


    Given("삭제 요청을 했고 유저 정보에 있는 유저 토큰으로") {
        val defaultUser = User(
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now()
        )
        val defaultDeletionRequest = DeletionRequest(
            userToken = "user1234",
            reason = "delete"
        )

        every { userRepository.findByUserToken(any()) } returns defaultUser
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), DeletionStatus.REQUEST) } returns defaultDeletionRequest

        When("복구할 때") {
            val command = RestoreUserCommand(defaultUser.userToken)
            val result = restoreUserService.restoreUser(command)

            Then("복구된다") {
                result.userToken shouldBe defaultUser.userToken
            }
        }
    }

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null

        When("복구할 때") {
            val command = RestoreUserCommand("user1234")

            Then("유저가 없다는 예외가 발생해서 복구할 수 없다") {
                shouldThrow<UserNotFoundException> {
                    restoreUserService.restoreUser(command)
                }
            }
        }
    }

    Given("삭제 요청을 하지 않았으면") {
        val defaultUser = User(
            name = "name",
            nickname = "nickname",
            email = Email("email@gmail.com"),
            phoneNumber = "+821012345678",
            birthday = LocalDate.now()
        )

        every { userRepository.findByUserToken(any()) } returns defaultUser
        every { deletionRequestRepository.findByUserTokenAndStatus(any(), DeletionStatus.REQUEST) } returns null

        When("복구할 때") {
            val command = RestoreUserCommand("user1234")

            Then("삭제 요청을 찾을 수 없다는 예외가 발생해서 복구할 수 없다") {
                shouldThrow<DeletionRequestNotFoundException> {
                    restoreUserService.restoreUser(command)
                }
            }
        }
    }
})
