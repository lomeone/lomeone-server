package com.lomeone.domain.memory.usecase

import com.lomeone.domain.memory.service.UploadImagesService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class CreateMemory(
    private val createPost: CreatePost,
    private val createPhoto: CreatePhoto,
    private val uploadImagesService: UploadImagesService
) {
    @Transactional
    fun execute(createMemoryRequest: CreateMemoryRequest): CreateMemoryResponse {
        val (title, content, visibility, placeName, placeAddress, firebaseUserToken, multipartFile) = createMemoryRequest
        val createPostRequest = CreatePostRequest(
            title = title,
            content = content,
            visibility = visibility,
            placeName = placeName,
            placeAddress = placeAddress,
            firebaseUserToken = firebaseUserToken
        )
        val createPostResponse = createPost.execute(createPostRequest)

        val urls = uploadImagesService.uploadImages(multipartFile)

        val createPhotoResponses: MutableList<CreatePhotoResponse> = mutableListOf()

        urls.forEach {
            val createPhotoRequest = CreatePhotoRequest(
                postId = createPostResponse.id,
                url = it.value
            )
            createPhotoResponses.add(createPhoto.execute(createPhotoRequest))
        }

        return CreateMemoryResponse(
            createPostResponse = createPostResponse,
            createPhotoResponses = createPhotoResponses
        )
    }
}

data class CreateMemoryRequest(
    val title: String,
    val content: String,
    val visibility: Boolean,
    val placeName: String,
    val placeAddress: String,
    val firebaseUserToken: String,
    val multipartFile: List<MultipartFile>
)

data class CreateMemoryResponse(
    val createPostResponse: CreatePostResponse,
    val createPhotoResponses: List<CreatePhotoResponse>
)
