package com.lomeone.infrastructure.config

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class AwsS3Config {
    @Value("\${cloud.aws.region.static}")
    private var region: String = "ap-northeast-2"

    @Value("\${cloud.aws.s3.bucket}")
    private var bucket: String = "mms-images-bucket"

    private var uploadPath: String = "data/images/"

    @Bean
    @Primary
    fun awsS3Client(): AmazonS3 = AmazonS3Client.builder().withRegion(region).build()

    @Bean
    fun awsS3Bucket(): String = bucket

    @Bean
    fun awsImageUploadPath(): String = uploadPath
}
