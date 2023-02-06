package io.github.comstering.infrasturcture.service

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class AwsS3Service(
    private val amazonS3: AmazonS3
) {
    private val bucket: String = "bucket"

    @Transactional
    fun uploadImages(multipartFiles: List<MultipartFile>): List<String> =
        multipartFiles.map { file ->
            val fileName = file.originalFilename ?: "null"
            val objectMetadata = ObjectMetadata()
            objectMetadata.contentLength = file.size
            objectMetadata.contentType = file.contentType

            amazonS3.putObject(
                PutObjectRequest(bucket, fileName, file.inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead)
            )
            amazonS3.getUrl(bucket, fileName).toString()
    }
}
