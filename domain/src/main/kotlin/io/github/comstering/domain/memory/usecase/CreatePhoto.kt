package io.github.comstering.domain.memory.usecase

import io.github.comstering.domain.memory.entity.Photo
import io.github.comstering.domain.memory.repository.PhotoRepository
import io.github.comstering.domain.memory.repository.PostRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.validation.constraints.NotBlank

@Service
class CreatePhoto(
    private val photoRepository: PhotoRepository,
    private val postRepository: PostRepository
) {
    fun execute(request: CreatePhotoRequest): CreatePhotoResponse {
        val (postId, url, isMain) = request

        val post = getPost(postId)

        val photo = photoRepository.save(
            Photo(
                post = post,
                url = url,
                isMain = isMain
            )
        )

        return CreatePhotoResponse(
            id = photo.id,
            url = photo.url,
            isMain = photo.isMain
        )
    }

    private fun getPost(postId: Long) =
        postRepository.findByIdOrNull(postId) ?: throw Exception("Post not found")
}

data class CreatePhotoRequest(
    val postId: Long,
    @field:NotBlank val url: String,
    val isMain: Boolean
)

data class CreatePhotoResponse(
    val id: Long,
    val url: String,
    val isMain: Boolean
)
