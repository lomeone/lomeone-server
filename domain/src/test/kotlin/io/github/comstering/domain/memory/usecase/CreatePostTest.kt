package io.github.comstering.domain.memory.usecase

import io.github.comstering.domain.memory.entity.Place
import io.github.comstering.domain.memory.entity.Post
import io.github.comstering.domain.memory.repository.PostRepository
import io.github.comstering.domain.user.entity.User
import io.github.comstering.domain.user.repository.UserRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CreatePostTest : BehaviorSpec({
    val postRepository: PostRepository = mockk()
    val userRepository: UserRepository = mockk()
    val createPost = CreatePost(postRepository, userRepository)

    Given("제목, 내용, 장소이름, 장소주소가 모두 공백이 아니고") {
        val titleInput = "title"
        val contentInput = "content"
        val placeNameInput = "placeName"
        val placeAddressInput = "placeAddress"
        And("유저가 존재하면") {
            val user = mockk<User>()
            every { userRepository.findByFirebaseUserToken(any()) } returns user
            When("해당 유저가 포스트를 생성할 때") {
                every { postRepository.save(any()) } returns Post(
                    title = titleInput,
                    content = contentInput,
                    place = Place(placeNameInput, placeAddressInput),
                    visibility = true,
                    user = user
                )

                val response = withContext(Dispatchers.IO) {
                    createPost.execute(
                        CreatePostRequest(
                            title = titleInput,
                            content = contentInput,
                            visibility = true,
                            placeName = placeNameInput,
                            placeAddress = placeAddressInput,
                            firebaseUserToken = "user1234"
                        )
                    )
                }

                Then("포스트가 생성된다") {
                    response.title shouldBe titleInput
                    response.content shouldBe contentInput
                    response.placeName shouldBe placeNameInput
                    response.placeAddress shouldBe placeAddressInput
                }
            }
        }
        And("유저가 존재하지 않으면") {
            every { userRepository.findByFirebaseUserToken(any()) } returns null
            When("포스트를 생성할 때") {
                val request = CreatePostRequest(
                    title = titleInput,
                    content = contentInput,
                    visibility = true,
                    placeName = placeNameInput,
                    placeAddress = placeAddressInput,
                    firebaseUserToken = "user1234"
                )

                Then("유저를 찾지 못했다는 예외가 발생해서 포스트를 생성할 수 없다") {
                    shouldThrow<Exception> {
                        createPost.execute(request)
                    }
                }
            }
        }
    }
})
