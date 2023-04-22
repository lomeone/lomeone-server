package com.lomeone.domain.post.entity

import com.lomeone.domain.user.entity.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.mockk

class PostTest : FreeSpec({
    val placeInput = Place("placeName", "placeAddress")
    val userInput: User = mockk()

    "포스트 생성할 때" - {
        "제목과 내용이 공백이 아니어야 생성할 수 있다" {
            val titleInput = "title"
            val contentInput = "content"

            val post = Post(
                title = titleInput,
                content = contentInput,
                place = placeInput,
                visibility = true,
                photos = emptyList(),
                user = userInput
            )

            post.title shouldBe titleInput
            post.content shouldBe contentInput
            post.place shouldBe placeInput
            post.user shouldBe userInput
        }

        "제목이 공백이면 생성할 수 없다" {
            val titleInput = ""
            val contentInput = "content"

            shouldThrow<IllegalArgumentException> {
                Post(
                    title = titleInput,
                    content = contentInput,
                    place = placeInput,
                    visibility = true,
                    photos = emptyList(),
                    user = userInput
                )
            }
        }

        "내용이 공백이면 생성할 수 없다" {
            val titleInput = "title"
            val contentInput = ""

            shouldThrow<IllegalArgumentException> {
                Post(
                    title = titleInput,
                    content = contentInput,
                    place = placeInput,
                    visibility = true,
                    photos = emptyList(),
                    user = userInput
                )
            }
        }
    }

    "포스트의 사진 리스트를 얻을 수 있다" {
        val photoInput = Photo(url = "photo1")
        val post = Post(
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = listOf(photoInput),
            user = userInput
        )

        post.photos.forEach { photo ->
            photo shouldBe photoInput
        }
    }

    "포스트 정보를 업데이트할 때" - {
        val post = Post(
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )

        "제목과 내용이 공백이 아니어야 업데이트할 수 있다." {
            val titleInput = "title"
            val contentInput = "content"

            post.updatePost(
                title = titleInput,
                content = contentInput,
                visibility = false,
                place = placeInput,
            )

            post.title shouldBe titleInput
            post.content shouldBe contentInput
            post.visibility shouldBe false
            post.place shouldBe placeInput
        }

        "제목이 공백이면 업데이트할 수 없다." {
            val titleInput = ""
            val contentInput = "content"

            shouldThrow<IllegalArgumentException> {
                post.updatePost(
                    title = titleInput,
                    content = contentInput,
                    visibility = false,
                    place = placeInput,
                )
            }
        }

        "내용이 공백이면 업데이트할 수 없다." {
            val titleInput = "title"
            val contentInput = ""

            shouldThrow<IllegalArgumentException> {
                post.updatePost(
                    title = titleInput,
                    content = contentInput,
                    visibility = false,
                    place = placeInput,
                )
            }
        }
    }

    "포스트를 삭제할 수 있다" {
        val post = Post(
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )

        post.delete()

        post.deleted shouldBe true
    }

    "포스트를 삭제하고 다시 복원할 수 있다." {
        val post = Post(
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )

        post.delete()
        post.deleted shouldBe true
        post.restore()
        post.deleted shouldBe false
    }

    "포스트 id가 같으면 같은 포스트이다" {
        val post1 = Post(
            id = 1L,
            title = "title1",
            content = "content1",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )

        val post2 = Post(
            id = 1L,
            title = "title2",
            content = "content2",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )

        (post1 == post2) shouldBe true
    }

    "포스트 객체가 아니면 다른 포스트이다" {
        val post1 = Post(
            id = 1L,
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )
        val post2 = "post2"

        post1 shouldNotBe post2
    }

    "포스트 id가 다르면 다른 포스트이다" {
        val post1 = Post(
            id = 1L,
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )
        val post2 = Post(
            id = 2L,
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )

        post1 shouldNotBe post2
    }

    "포스트 id가 같으면 hash 값도 같다" {
        val post1 = Post(
            id = 1L,
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )
        val post2 = Post(
            id = 1L,
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )

        post1.hashCode() shouldBe post2.hashCode()
    }

    "포스트 id가 다르면 hash 값도 다르다" {
        val post1 = Post(
            id = 1L,
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )
        val post2 = Post(
            id = 2L,
            title = "title",
            content = "content",
            place = placeInput,
            visibility = true,
            photos = emptyList(),
            user = userInput
        )

        post1.hashCode() shouldNotBe post2.hashCode()
    }
})

class PlaceTest : FreeSpec({
    "장소 이름과 주소에 공백이 없으면 장소를 생성할 수 있다" {
        val name = "name"
        val address = "address"

        val place = Place(name, address)

        place.name shouldBe name
        place.address shouldBe address
    }

    "장소 이름이 공백이면 장소 이름이 공백이라는 예외가 발생해서 장소를 생성할 수 없다" {
        val name = ""
        val address = "address"

        shouldThrow<IllegalArgumentException> {
            Place(name, address)
        }
    }

    "장소 주소가 공백이면 장소 주소가 공백이라는 예외가 발생해서 장소를 생성할 수 없다" {
        val name = "name"
        val address = ""

        shouldThrow<IllegalArgumentException> {
            Place(name, address)
        }
    }
})
