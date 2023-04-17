package com.lomeone.domain.post.usecase

import com.lomeone.domain.post.entity.Place
import com.lomeone.domain.post.entity.Post
import com.lomeone.domain.post.repository.PostRepository
import com.lomeone.domain.post.service.UploadImagesService
import com.lomeone.domain.post.service.Url
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.web.multipart.MultipartFile

class CreatePostTest : BehaviorSpec({
    val postRepository: PostRepository = mockk()
    val userRepository: UserRepository = mockk()
    val uploadImagesService: UploadImagesService = mockk()
    val createPost = CreatePost(postRepository, userRepository, uploadImagesService)

    Given("제목, 내용, 공개여부, 장소, 작성자, 이미지파일들이 주어지고") {
        val titleInput = "title"
        val contentInput = "content"
        val visibilityInput = true
        val placeNameInput = "placeName"
        val placeAddressInput = "placeAddress"
        val userTokenInput = "userToken"
        val multipartFileInput = listOf<MultipartFile>()

        And("유저가 존재하고") {
            val user: User = mockk()
            every { userRepository.findByUserToken(any()) } returns user

            And("이미지 파일 업로드에 성공하면") {
                val urls = listOf<Url>()
                every { uploadImagesService.uploadImages(any()) } returns urls

                When("포스트를 생성할 때") {
                    every { postRepository.save(any()) } returns Post(
                        id = 1L,
                        title = titleInput,
                        content = contentInput,
                        visibility = visibilityInput,
                        place = Place(placeNameInput, placeAddressInput),
                        photos = mutableListOf(),
                        user = user
                    )

                    val request = CreatePostRequest(
                        title = titleInput,
                        content = contentInput,
                        visibility = visibilityInput,
                        placeName = placeNameInput,
                        placeAddress = placeAddressInput,
                        userToken = userTokenInput,
                        multipartFiles = multipartFileInput
                    )

                    val response = createPost.execute(request)

                    Then("포스트가 생성된다") {
                        response.id shouldBe 1L
                    }
                }
            }
        }

        And("유저가 존재하지 않으면") {
            every { userRepository.findByUserToken(any()) } returns null

            When("포스트를 생성할 때") {
                val request = CreatePostRequest(
                    title = titleInput,
                    content = contentInput,
                    visibility = visibilityInput,
                    placeName = placeNameInput,
                    placeAddress = placeAddressInput,
                    userToken = userTokenInput,
                    multipartFiles = multipartFileInput
                )

                Then("유저가 없다는 예외가 발생해서 포스트 생성에 실패한다") {
                    shouldThrow<Exception> {
                        createPost.execute(request)
                    }
                }
            }
        }
    }
})
