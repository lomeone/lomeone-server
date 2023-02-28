package com.lomeone.domain.memory.entity

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class PhotoTest : FreeSpec({
    val postInput = mockk<Post>()
    "사진 생성할 때" - {
        "url이 공백이 아니면 생성할 수 있다" {
            val urlInput = "url"
            val photo = Photo(
                post = postInput,
                url = urlInput
            )
            photo.url shouldBe urlInput
            photo.post shouldBe postInput
        }
        "url이 공백이면 생성할 수 없다" {
            val urlInput = ""
            shouldThrow<IllegalArgumentException> {
                Photo(
                    post = postInput,
                    url = urlInput
                )
            }
        }
    }

    "사진을 제거하면 Soft Delete가 된다" {
        val photo = Photo(
            post = postInput,
            url = "url"
        )

        photo.delete()

        photo.deleted shouldBe true
    }

    "사진을 다시 복원할 수 있다" {
        val photo = Photo(
            post = postInput,
            url = "url"
        )

        photo.delete()
        photo.restore()

        photo.deleted shouldBe false
    }
})
