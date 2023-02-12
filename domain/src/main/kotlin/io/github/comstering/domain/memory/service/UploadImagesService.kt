package io.github.comstering.domain.memory.service

import org.springframework.web.multipart.MultipartFile

interface UploadImagesService {
    fun uploadImages(multipartFiles: List<MultipartFile>): List<String>
}
