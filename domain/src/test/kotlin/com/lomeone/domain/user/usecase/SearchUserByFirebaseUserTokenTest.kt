package com.lomeone.domain.user.usecase

import com.lomeone.domain.user.entity.AccountType
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.ZoneId
import java.time.ZonedDateTime

class SearchUserByFirebaseUserTokenTest : BehaviorSpec({
    val userRepository: UserRepository = mockk()
    val searchUserByFirebaseUserToken = SearchUserByFirebaseUserToken(userRepository)

    Given("firebaseUserToken을 가지고 있는 유저가 존재하면") {
        val firebaseInput = "user1234"
        every { userRepository.findByFirebaseUserToken(firebaseInput) } returns User(
            firebaseUserToken = firebaseInput,
            name = "name",
            nickname = "nickname",
            email = "email@gmail.com",
            birthday = ZonedDateTime.of(2000, 1, 1, 0, 0, 0, 0, ZoneId.of("Asia/Seoul")),
            photoUrl = "https://photo.com",
            accountType = AccountType.GOOGLE
        )
        When("유저를 검색할 때") {
            val response = withContext(Dispatchers.IO) {
                searchUserByFirebaseUserToken.execute(SearchUserByFirebaseUserTokenRequest(firebaseInput))
            }
            Then("유저가 검색된다") {
                response.user.firebaseUserToken shouldBe firebaseInput
            }
        }
    }
    Given("firebaseUserToken을 가지고 있는 유저가 존재하지 않으면") {
        val firebaseInput = "user1234"
        every { userRepository.findByFirebaseUserToken(firebaseInput) } returns null
        When("유저를 검색할 때") {
            Then("유저를 찾을 수 없다는 예외가 발생해서 유저를 검색할 수 없다") {
                shouldThrow<Exception> {
                    searchUserByFirebaseUserToken.execute(SearchUserByFirebaseUserTokenRequest(firebaseInput))
                }
            }
        }
    }
})
