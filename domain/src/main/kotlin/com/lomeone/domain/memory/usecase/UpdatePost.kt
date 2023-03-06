package com.lomeone.domain.memory.usecase

import com.lomeone.domain.memory.entity.Place
import com.lomeone.domain.memory.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UpdatePost(
    private val postRepository: PostRepository
) {
    @Transactional
    fun execute(request: UpdatePostRequest): UpdatePostResponse {
        val (id, title, content, visibility, placeName, placeAddress) = request

        val post = getPost(id)

        post.updatePost(title, content, visibility, Place(placeName, placeAddress))

        return UpdatePostResponse(
            id = post.id,
            title = post.title,
            content = post.content,
            visibility = post.visibility,
            placeName = post.place.name,
            placeAddress = post.place.address
        )
    }

    private fun getPost(id: Long) = postRepository.findById(id).orElseThrow { Exception("Post not found") }
}

data class UpdatePostRequest(
    val id: Long,
    val title: String,
    val content: String,
    val visibility: Boolean,
    val placeName: String,
    val placeAddress: String
)

data class UpdatePostResponse(
    val id: Long,
    val title: String,
    val content: String,
    val visibility: Boolean,
    val placeName: String,
    val placeAddress: String
)
