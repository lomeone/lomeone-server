package io.github.comstering.infrasturcture.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.services.s3.AmazonS3Client
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AwsS3Config {
    private val accessKey: String = "access"
    private val secretKey: String = "secret"
    private val region: String = "ap-northeast-2"

    @Bean
    fun awsS3Client(): AmazonS3Client {
        val credential = AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey))
        return AmazonS3Client.builder()
            .withCredentials(credential)
            .withRegion(region)
            .build() as AmazonS3Client
    }
}
