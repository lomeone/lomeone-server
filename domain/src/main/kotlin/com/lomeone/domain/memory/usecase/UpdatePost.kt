package com.lomeone.domain.memory.usecase

class UpdatePost {
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
