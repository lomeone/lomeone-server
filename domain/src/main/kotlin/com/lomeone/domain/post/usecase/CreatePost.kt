package com.lomeone.domain.post.usecase

import com.lomeone.domain.post.entity.Photo
import com.lomeone.domain.post.entity.Place
import com.lomeone.domain.post.entity.Post
import com.lomeone.domain.post.repository.PostRepository
import com.lomeone.domain.user.entity.User
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
        val (title, content, visibility, placeName, placeAddress, userToken, photoUrls) = request

        val user = getUser(userToken)
        val place = Place(placeName, placeAddress)

        val post = postRepository.save(
            Post(
                title = title,
                content = content,
                visibility = visibility,
                place = place,
                user = user,
                photos = photoUrls.map { Photo(url = it) }.toMutableList()
            )
        )
        return CreatePostResponse(
            id = post.id
        )
    }
    private fun getUser(userToken: String): User =
        userRepository.findByUserToken(userToken) ?: throw Exception("User not found")
}

data class CreatePostRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val content: String,
    val visibility: Boolean,
    @field:NotBlank val placeName: String,
    @field:NotBlank val placeAddress: String,
    @field:NotBlank val userToken: String,
    @field:NotBlank val photoUrls: List<String>
)

data class CreatePostResponse(
    val id: Long
)
