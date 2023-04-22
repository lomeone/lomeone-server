package com.lomeone.domain.post.service

import org.springframework.web.multipart.MultipartFile

interface UploadImagesService {
    fun uploadImages(multipartFiles: List<MultipartFile>): List<Url>
}

data class Url(val value: String)
