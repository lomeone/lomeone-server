package com.lomeone.domain.memory.usecase

import com.lomeone.domain.memory.entity.Place
import com.lomeone.domain.memory.entity.Post
import com.lomeone.domain.memory.repository.PostRepository
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.validation.constraints.NotBlank

@Service
class CreatePost(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository
) {
    @Transactional
    fun execute(request: CreatePostRequest): CreatePostResponse {
        val (title, content, visibility, placeName, placeAddress, userToken) = request
        val user = getUser(userToken)
        val place = Place(placeName, placeAddress)
        val post = postRepository.save(
            Post(
                title = title,
                content = content,
                visibility = visibility,
                place = place,
                user = user
            )
        )
        return CreatePostResponse(
            id = post.id,
            title = post.title,
            content = post.content,
            visibility = post.visibility,
            placeName = post.place.name,
            placeAddress = post.place.address
        )
    }

    private fun getUser(userToken: String) =
        userRepository.findByUserToken(userToken) ?: throw Exception("User not found")
}

data class CreatePostRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val content: String,
    val visibility: Boolean,
    @field:NotBlank val placeName: String,
    @field:NotBlank val placeAddress: String,
    @field:NotBlank val userToken: String
)

data class CreatePostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val visibility: Boolean,
    val placeName: String,
    val placeAddress: String
)
