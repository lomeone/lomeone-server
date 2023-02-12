package io.github.comstering.infrasturcture.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import io.github.comstering.domain.memory.service.UploadImagesService
import io.github.comstering.domain.memory.service.Url
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@Service
class AwsS3Service(
    private val amazonS3: AmazonS3
) : UploadImagesService {
    private val bucket: String = "bucket"

    override fun uploadImages(multipartFiles: List<MultipartFile>): List<Url> =
        multipartFiles.map { file ->
            val fileName = getRandomFileName(file.originalFilename ?: "null")
            val objectMetadata = ObjectMetadata()
            objectMetadata.contentLength = file.size
            objectMetadata.contentType = file.contentType

            amazonS3.putObject(
                PutObjectRequest(bucket, fileName, file.inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
            Url(amazonS3.getUrl(bucket, fileName).toString())
        }

    private fun getRandomFileName(fileName: String): String {
        checkImageExtension(fileName)
        return UUID.randomUUID().toString() + "_" + fileName
    }

    private fun checkImageExtension(fileName: String) {
        val imageFileNamePattern = "(.*?)\\.(jpg|jpeg|png|gif|bmp)\$"
        val regex = Regex(imageFileNamePattern)
        !fileName.matches(regex) && throw Exception("Is not image file")
    }
}
