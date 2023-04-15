package com.lomeone.domain.post.usecase

import com.lomeone.domain.post.entity.Photo
import com.lomeone.domain.post.entity.Place
import com.lomeone.domain.post.entity.Post
import com.lomeone.domain.post.repository.PostRepository
import com.lomeone.domain.post.service.UploadImagesService
import com.lomeone.domain.user.entity.User
import com.lomeone.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import javax.validation.constraints.NotBlank

@Service
class CreatePost(
    private val postRepository: PostRepository,
    private val userRepository: UserRepository,
    private val uploadImagesService: UploadImagesService
) {
    @Transactional
    fun execute(request: CreatePostRequest): CreatePostResponse {
        val (title, content, visibility, placeName, placeAddress, userToken, multipartFiles) = request

        val user = getUser(userToken)
        val place = Place(placeName, placeAddress)
        val photos = getPhotos(multipartFiles)

        val post = postRepository.save(
            Post(
                title = title,
                content = content,
                visibility = visibility,
                place = place,
                user = user,
                photos = photos
            )
        )

        return CreatePostResponse(
            id = post.id
        )
    }
    private fun getUser(userToken: String): User =
        userRepository.findByUserToken(userToken) ?: throw Exception("User not found")

    private fun getPhotos(multipartFiles: List<MultipartFile>): MutableList<Photo> {
        val photoUrls = uploadImagesService.uploadImages(multipartFiles)
        return photoUrls.map { Photo(url = it.value) }.toMutableList()
    }
}

data class CreatePostRequest(
    @field:NotBlank val title: String,
    @field:NotBlank val content: String,
    val visibility: Boolean,
    @field:NotBlank val placeName: String,
    @field:NotBlank val placeAddress: String,
    @field:NotBlank val userToken: String,
    val multipartFiles: List<MultipartFile>
)

data class CreatePostResponse(
    val id: Long
)
