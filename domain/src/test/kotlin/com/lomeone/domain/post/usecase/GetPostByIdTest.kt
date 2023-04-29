package com.lomeone.domain.post.usecase

import com.lomeone.domain.post.entity.Place
import com.lomeone.domain.post.entity.Post
import com.lomeone.domain.post.repository.PostRepository
import com.lomeone.domain.user.entity.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import java.util.*

class GetPostByIdTest : BehaviorSpec({
    val postRepository: PostRepository = mockk()
    val getPostById = GetPostById(postRepository)

    val user: User = mockk()
    every { user.userToken } returns "userToken"

    Given("id가 주어지고") {
        val idInput = 1L
        And("포스트가 존재하면") {
            every { postRepository.findById(any()) } returns Optional.of(Post(
                id = 1L,
                title = "title",
                content = "content",
                visibility = true,
                place = Place(name = "placeName", address = "placeAddress"),
                photos = listOf(),
                user = user
            ))
            When("포스트를 조회할 때") {
                val query = GetPostByIdQuery(id = idInput)
                val result = getPostById.execute(query)
                Then("포스트를 반환한다") {
                    result.id shouldBe idInput
                    result.title shouldBe "title"
                    result.content shouldBe "content"
                    result.visibility shouldBe true
                    result.placeName shouldBe "placeName"
                    result.placeAddress shouldBe "placeAddress"
                    result.photos shouldBe listOf()
                    result.userToken shouldBe "userToken"
                }
            }
        }

        And("포스트가 존재하지 않으면") {
            every { postRepository.findById(any()) } returns Optional.empty()
            When("포스트를 조회할 때") {
                val query = GetPostByIdQuery(id = idInput)
                Then("예외를 던진다") {
                    shouldThrow<Exception> {
                        getPostById.execute(query)
                    }
                }
            }
        }
    }
})
