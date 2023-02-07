package io.github.comstering.infrastructure.service

import com.amazonaws.services.s3.AmazonS3
import io.github.comstering.infrasturcture.service.AwsS3Service
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.web.multipart.MultipartFile

class AwsS3ServiceTest : BehaviorSpec({
    val amazonS3: AmazonS3 = mockk()
    val awsS3Service = AwsS3Service(amazonS3)

    Given("파일 리스트의 파일이 모두 이미지파일이면") {
        val multipartFile = mockk<MultipartFile>()
        every { multipartFile.originalFilename } returns "test.jpg"
        every { multipartFile.size } returns 1024
        every { multipartFile.contentType } returns "image/jpeg"
        every { multipartFile.inputStream } returns mockk()

        val multipartFiles = listOf(multipartFile)

        When("파일 리스트를 업로드할 때") {
            every { amazonS3.putObject(any()) } returns mockk()
            every { amazonS3.getUrl(any(), any()) } returns mockk()

            val result = withContext(Dispatchers.IO) {
                awsS3Service.uploadImages(multipartFiles)
            }
            Then("파일이 업로드 된다") {
                verify { amazonS3.putObject(any()) }
                result.size shouldBe 1
            }
        }
    }

    Given("파일 리스트의 파일 중 이미지 파일이 아닌 파일이 있으면") {
        val multipartFile = mockk<MultipartFile>()
        every { multipartFile.originalFilename } returns "test.pdf"
        every { multipartFile.size } returns 1024
        every { multipartFile.contentType } returns "application/pdf"
        every { multipartFile.inputStream } returns mockk()

        val multipartFiles = listOf(multipartFile)

        When("파일 리스트를 업로드할 때") {
            every { amazonS3.putObject(any()) } returns mockk()
            every { amazonS3.getUrl(any(), any()) } returns mockk()
            Then("이미지파일이 아니라는 예외가 발생해서 파일이 업로드되지 않는다") {
                shouldThrow<Exception> {
                    awsS3Service.uploadImages(multipartFiles)
                }
            }
        }
    }
})
