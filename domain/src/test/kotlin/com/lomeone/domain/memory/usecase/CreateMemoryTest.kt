package com.lomeone.domain.memory.usecase

import com.lomeone.domain.memory.service.UploadImagesService
import com.lomeone.domain.memory.service.Url
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.multipart.MultipartFile

class CreateMemoryTest : BehaviorSpec({
    val createPost: CreatePost = mockk()
    val createPhoto: CreatePhoto = mockk()
    val uploadImagesService: UploadImagesService = mockk()
    val createMemory = CreateMemory(createPost, createPhoto, uploadImagesService)

    Given("제목, 내용, 공개여부, 장소정보, 유정토큰, 이미지파일이 주어지면") {
        val titleInput = "title"
        val contentInput = "content"
        val visibilityInput = true
        val placeNameInput = "placeName"
        val placeAddressInput = "placeAddress"
        val firebaseUserTokenInput = "firebaseUserToken"
        val multipartFileInput = listOf<MultipartFile>()

        When("Memory를 만들 때") {
            every { createPost.execute(any()) } returns CreatePostResponse(
                id = 1,
                title = titleInput,
                content = contentInput,
                visibility = visibilityInput,
                placeName = placeNameInput,
                placeAddress = placeAddressInput
            )
            every { uploadImagesService.uploadImages(any()) } returns listOf(Url("url"))
            every { createPhoto.execute(any()) } returns CreatePhotoResponse(
                id = 1,
                url = "url"
            )

            val response = withContext(Dispatchers.IO) {
                createMemory.execute(
                    CreateMemoryRequest(
                        title = titleInput,
                        content = contentInput,
                        visibility = visibilityInput,
                        placeName = placeNameInput,
                        placeAddress = placeAddressInput,
                        firebaseUserToken = firebaseUserTokenInput,
                        multipartFile = multipartFileInput
                    )
                )
            }
            Then("Memory가 만들어진다") {
                response.createPostResponse.id shouldBe 1
                response.createPostResponse.title shouldBe titleInput
                response.createPostResponse.content shouldBe contentInput
                response.createPostResponse.visibility shouldBe visibilityInput
                response.createPostResponse.placeName shouldBe placeNameInput
                response.createPostResponse.placeAddress shouldBe placeAddressInput
                response.createPhotoResponses.size shouldBe 1
                response.createPhotoResponses[0].id shouldBe 1
                response.createPhotoResponses[0].url shouldBe "url"
            }
        }
    }
})
