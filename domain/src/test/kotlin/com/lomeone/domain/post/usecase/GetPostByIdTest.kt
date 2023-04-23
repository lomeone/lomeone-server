package com.lomeone.domain.post.usecase

import com.lomeone.domain.post.entity.Place
import com.lomeone.domain.post.entity.Post
import com.lomeone.domain.post.repository.PostRepository
import com.lomeone.domain.user.entity.User
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
                val request = GetPostByIdRequest(id = idInput)
                val response = getPostById.execute(request)
                Then("포스트를 반환한다") {
                    response.id shouldBe idInput
                    response.title shouldBe "title"
                    response.content shouldBe "content"
                    response.visibility shouldBe true
                    response.placeName shouldBe "placeName"
                    response.placeAddress shouldBe "placeAddress"
                    response.photos shouldBe listOf()
                    response.userToken shouldBe "userToken"
                }
            }
        }
    }
})
