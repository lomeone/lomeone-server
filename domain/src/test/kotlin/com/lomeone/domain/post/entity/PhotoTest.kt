package com.lomeone.domain.post.entity

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

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

    "사진 객체가 아니면 다른 사진이다" {
        val photo = Photo(
            url = "url"
        )
        photo shouldNotBe "photo"
    }

    "사진 id가 다르면 다른 사진이다" {
        val photo1 = Photo(
            id = 1L,
            url = "url"
        )
        val photo2 = Photo(
            id = 2L,
            url = "url"
        )

        photo1 shouldNotBe photo2
    }

    "사진 id가 같으면 같은 사진이다" {
        val photo1 = Photo(
            id = 1L,
            url = "url1"
        )
        val photo2 = Photo(
            id = 1L,
            url = "url2"
        )

        photo1 shouldBe photo2
    }

    "사진 id가 같으면 hash 값도 같다" {
        val photo1 = Photo(
            id = 1L,
            url = "url1"
        )
        val photo2 = Photo(
            id = 1L,
            url = "url2"
        )

        photo1.hashCode() shouldBe photo2.hashCode()
    }

    "사진 id가 다르면 hash 값도 다르다" {
        val photo1 = Photo(
            id = 1L,
            url = "url"
        )
        val photo2 = Photo(
            id = 2L,
            url = "url"
        )

        photo1.hashCode() shouldNotBe photo2.hashCode()
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
