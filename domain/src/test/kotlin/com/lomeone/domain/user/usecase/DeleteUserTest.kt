package com.lomeone.domain.user.usecase

import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DeleteUserTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val deleteUser = DeleteUser(userRepository)

    val user: User = mockk()

    Given("유저가 존재하면") {
        every { userRepository.findByUserToken(any()) } returns user
        every { user.inactivate() } returns Unit
        every { user.userToken } returns "user1234"

        When("유저를 삭제할 때") {
            val command = DeleteUserCommand("user1234")

            withContext(Dispatchers.IO) {
                deleteUser.execute(command)
            }
            Then("유저가 삭제된다") {
                verify { user.inactivate() }
            }
        }
    }

    Given("유저가 존재하지 않으면") {
        every { userRepository.findByUserToken(any()) } returns null
        When("유저를 삭제할 때") {
            val command = DeleteUserCommand("user1234")
            Then("유저를 찾을 수 없다는 예외가 발생해서 유저를 삭제할 수 없다") {
                shouldThrow<Exception> {
                    deleteUser.execute(command)
                }
            }
        }
    }
})
