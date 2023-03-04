package com.lomeone.domain.memory.usecase

import com.lomeone.domain.memory.entity.Place
import com.lomeone.domain.memory.entity.Post
import com.lomeone.domain.memory.repository.PostRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

class UpdatePostTest : BehaviorSpec({
    val postRepository: PostRepository = mockk()
    val updatePost = UpdatePost(postRepository)

    Given("id에 해당하는 포스트가 있으면") {
        val postIdInput = 1L

        every { postRepository.findById(postIdInput) } returns Optional.of(
            Post(
                id = postIdInput,
                title = "test",
                content = "test",
                visibility = true,
                place = Place(
                    name = "placeName",
                    address = "placeAddress"
                ),
                user = mockk()
            )
        )

        When("포스트를 업데이트할 때") {
            val request = UpdatePostRequest(
                id = postIdInput,
                title = "update",
                content = "update content",
                visibility = false,
                placeName = "updatePlace",
                placeAddress = "updateAddress"
            )

            val response = withContext(Dispatchers.IO) {
                updatePost.execute(request)
            }

            Then("포스트가 업데이트 된다.") {
                response.id shouldBe postIdInput
                response.title shouldBe request.title
                response.content shouldBe request.content
                response.visibility shouldBe request.visibility
                response.placeName shouldBe request.placeName
                response.placeAddress shouldBe request.placeAddress
            }
        }
    }
})
