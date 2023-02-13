package io.github.comstering.domain.memory.usecase

import io.github.comstering.domain.memory.entity.Photo
import io.github.comstering.domain.memory.entity.Post
import io.github.comstering.domain.memory.repository.PhotoRepository
import org.springframework.stereotype.Service
import javax.validation.constraints.NotBlank

@Service
class CreatePhoto(
    private val photoRepository: PhotoRepository
) {
    fun execute(request: CreatePhotoRequest): CreatePhotoResponse {
        val (post, url, isMain) = request

        val photo = Photo(
            post = post,
            url = url,
            isMain = isMain
        )

        val savedPhoto = photoRepository.save(photo)

        return CreatePhotoResponse(
            id = savedPhoto.id,
            url = savedPhoto.url,
            isMain = savedPhoto.isMain
        )
    }
}

data class CreatePhotoRequest(
    val post: Post,
    @field:NotBlank val url: String,
    val isMain: Boolean
)

data class CreatePhotoResponse(
    val id: Long,
    val url: String,
    val isMain: Boolean
)
