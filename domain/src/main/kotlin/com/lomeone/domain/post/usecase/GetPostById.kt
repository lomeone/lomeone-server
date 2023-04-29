package com.lomeone.domain.post.usecase

import com.lomeone.domain.post.entity.Post
import com.lomeone.domain.post.repository.PostRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetPostById(
    private val postRepository: PostRepository
) {
    @Transactional(readOnly = true)
    fun execute(request: GetPostByIdQuery): GetPostByIdResult {
        val post = getPost(request.id)

        return GetPostByIdResult(
            id = post.id,
            title = post.title,
            content = post.content,
            visibility = post.visibility,
            placeName = post.place.name,
            placeAddress = post.place.address,
            photos = post.photos.map { it.url },
            userToken = post.user.userToken
        )
    }

    private fun getPost(id: Long): Post =
        postRepository.findById(id).orElseThrow { Exception("Post not found") }
}

data class GetPostByIdQuery(
    val id: Long
)

data class GetPostByIdResult(
    val id: Long,
    val title: String,
    val content: String,
    val visibility: Boolean,
    val placeName: String,
    val placeAddress: String,
    val photos: List<String>,
    val userToken: String
)
