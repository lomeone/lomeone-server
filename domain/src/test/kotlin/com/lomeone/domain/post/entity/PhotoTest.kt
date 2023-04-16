package com.lomeone.domain.post.entity

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class PhotoTest : FreeSpec({
    "사진 생성할 때" - {
        "url이 공백이 아니면 생성할 수 있다" {
            val urlInput = "url"
            val photo = Photo(
                url = urlInput
            )
            photo.url shouldBe urlInput
        }
        "url이 공백이면 생성할 수 없다" {
            val urlInput = ""
            shouldThrow<IllegalArgumentException> {
                Photo(
                    url = urlInput
                )
            }
        }
    }

    "사진을 제거하면 Soft Delete 된다" {
        val photo = Photo(
            url = "url"
        )

        photo.delete()

        photo.deleted shouldBe true
    }

    "사진을 다시 복원할 수 있다" {
        val photo = Photo(
            url = "url"
        )

        photo.delete()
        photo.restore()

        photo.deleted shouldBe false
    }
})
