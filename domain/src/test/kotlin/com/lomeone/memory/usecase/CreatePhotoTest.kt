package com.lomeone.memory.usecase

import com.lomeone.domain.memory.entity.Photo
import com.lomeone.domain.memory.entity.Post
import com.lomeone.domain.memory.repository.PhotoRepository
import com.lomeone.domain.memory.repository.PostRepository
import com.lomeone.domain.memory.usecase.CreatePhoto
import com.lomeone.domain.memory.usecase.CreatePhotoRequest
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.data.repository.findByIdOrNull

class CreatePhotoTest : BehaviorSpec({
    val photoRepository: PhotoRepository = mockk()
    val postRepository: PostRepository = mockk()
    val createPhoto = CreatePhoto(photoRepository, postRepository)

    Given("사진의 url이 공백이 아니고") {
        val urlInput = "url"
        And("포스트가 존재하면") {
            val post = mockk<Post>()
            every { postRepository.findByIdOrNull(any()) } returns post
            When("해당 포스트에 사진을 추가할 때") {
                val request = CreatePhotoRequest(
                    postId = 1,
                    url = urlInput
                )

                every { photoRepository.save(any()) } returns Photo(
                    id = 1L,
                    url = urlInput,
                    post = post
                )

                val response = withContext(Dispatchers.IO) {
                    createPhoto.execute(request)
                }
                Then("사진이 추가된다") {
                    response.id shouldBe 1L
                    response.url shouldBe urlInput
                }
            }
        }
        And("포스트가 존재하지 않으면") {
            every { postRepository.findByIdOrNull(any()) } returns null
            When("해당 포스트에 사진을 추가할 때") {
                val request = CreatePhotoRequest(
                    postId = 1,
                    url = urlInput
                )
                Then("Post가 없다는 예외가 발생해서 사진이 추가되지 않는다") {
                    shouldThrow<Exception> {
                        createPhoto.execute(request)
                    }
                }
            }
        }
    }
})
