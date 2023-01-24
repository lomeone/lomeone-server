package io.github.comstering.domain.memory.entity

import io.github.comstering.domain.memory.entity.Place
import io.github.comstering.domain.memory.entity.Post
import io.github.comstering.domain.user.entity.AccountType
import io.github.comstering.domain.user.entity.User
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import java.time.ZoneId
import java.time.ZonedDateTime

class PostTest : FreeSpec({
    val placeInput = Place("placeName", "placeAddress")
    val userInput = User(
        firebaseUserToken = "user1234",
        name = "name",
        nickname = "nickname",
        email = "email@gmail.com",
        birthday = ZonedDateTime.now(ZoneId.of("Asia/Seoul")),
        accountType = AccountType.GOOGLE
    )
    "포스트 생성할 때" - {
        "제목과 내용이 공백이 아니어야 생성할 수 있다" - {
            val titleInput = "title"
            val contentInput = "content"

            val post = Post(
                title = titleInput,
                content = contentInput,
                place = placeInput,
                user = userInput
            )

            post.title shouldBe titleInput
            post.content shouldBe contentInput
            post.place shouldBe placeInput
            post.user shouldBe userInput
        }

        "제목이 공백이면 생성할 수 없다" - {
            val titleInput = ""
            val contentInput = "content"

            shouldThrow<IllegalArgumentException> {
                Post(
                    title = titleInput,
                    content = contentInput,
                    place = placeInput,
                    user = userInput
                )
            }
        }

        "내용이 공백이면 생성할 수 없다" - {
            val titleInput = "title"
            val contentInput = ""

            shouldThrow<IllegalArgumentException> {
                Post(
                    title = titleInput,
                    content = contentInput,
                    place = placeInput,
                    user = userInput
                )
            }
        }
    }

    "포스트의 정보를 업데이트할 때" - {
        val post = Post(
            title = "title",
            content = "content",
            place = placeInput,
            user = userInput
        )
        "제목과 내용이 공백이 아니어야 업데이트 할 수 있다" - {
            val titleInput = "title"
            val contentInput = "content"

            post.updatePost(titleInput, contentInput, placeInput)

            post.title shouldBe titleInput
            post.content shouldBe contentInput
            post.place shouldBe placeInput
        }
        "제목이 공백이면 업데이트 할 수 없다" - {
            val titleInput = ""
            val contentInput = "content"

            shouldThrow<IllegalArgumentException> {
                post.updatePost(titleInput, contentInput, placeInput)
            }
        }

        "내용이 공백이면 업데이트 할 수 없다" - {
            val titleInput = "title"
            val contentInput = ""

            shouldThrow<IllegalArgumentException> {
                post.updatePost(titleInput, contentInput, placeInput)
            }
        }
    }

    "포스트 id가 같은면 같은 포스트이다." - {
        val post1 = Post(
            id = 1L,
            title = "title1",
            content = "content1",
            place = placeInput,
            user = userInput
        )

        val post2 = Post(
            id = 1L,
            title = "title2",
            content = "content2",
            place = placeInput,
            user = userInput
        )
        (post1 == post2) shouldBe true
    }

    "포스트 id가 다르면 다른 포스트이다" - {
        val post1 = Post(
            id = 1L,
            title = "title1",
            content = "content1",
            place = placeInput,
            user = userInput
        )

        val post2 = Post(
            id = 2L,
            title = "title1",
            content = "content1",
            place = placeInput,
            user = userInput
        )
        (post1 == post2) shouldBe false
    }

    "포스트 객체가 아니면 다른 포스트이다" - {
        val post1 = Post(
            id = 1L,
            title = "title1",
            content = "content1",
            place = placeInput,
            user = userInput
        )

        val post2 = "post2"
        (post1.equals(post2)) shouldBe false
    }

    "포스트 id가 같으면 hash 값도 같다" - {
        val post1 = Post(
            id = 1L,
            title = "title1",
            content = "content1",
            place = placeInput,
            user = userInput
        )

        val post2 = Post(
            id = 1L,
            title = "title2",
            content = "content2",
            place = placeInput,
            user = userInput
        )

        (post1.hashCode() == post2.hashCode()) shouldBe true
    }

    "포스트 id가 다르면 hash 값도 다르다" - {
        val post1 = Post(
            id = 1L,
            title = "title1",
            content = "content1",
            place = placeInput,
            user = userInput
        )

        val post2 = Post(
            id = 2L,
            title = "title1",
            content = "content1",
            place = placeInput,
            user = userInput
        )

        (post1.hashCode() == post2.hashCode()) shouldBe false
    }
})

class PlaceTest : FreeSpec({
    "장소 이름과 주소에 공백이 없으면 장소를 생성할 수 있다" - {
        val nameInput = "name"
        val addressInput = "address"

        val place = Place(nameInput, addressInput)

        place.name shouldBe nameInput
        place.address shouldBe addressInput
    }

    "장소 이름에 공백이 있으면 장소 이름이 공백이라는 예외가 발생해서 장소를 생성할 수 없다" - {
        val nameInput = " "
        val addressInput = "address"

        shouldThrow<IllegalArgumentException> {
            Place(nameInput, addressInput)
        }
    }

    "장소 주소에 공백이 있으면 장소 주소가 공백이라는 예외가 발생해서 장소를 생성할 수 없다" - {
        val nameInput = "name"
        val addressInput = " "

        shouldThrow<IllegalArgumentException> {
            Place(nameInput, addressInput)
        }
    }
})
