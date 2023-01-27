package io.github.comstering.domain.memory.entity

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk

class PhotoTest : FreeSpec({
    val postInput = mockk<Post>()
    "사진 생성할 때" - {
        "url이 공백이 아니면 생성할 수 있다" - {
            val urlInput = "url"
            val photo = Photo(
                post = postInput,
                url = urlInput,
                isMain = false
            )
            photo.url shouldBe urlInput
            photo.post shouldBe postInput
        }
    }
})
