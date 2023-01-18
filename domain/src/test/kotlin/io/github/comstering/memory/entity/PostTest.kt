package io.github.comstering.memory.entity

import io.github.comstering.user.entity.AccountType
import io.github.comstering.user.entity.User
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
    "제목과 내용이 공백이 아니면 포스트가 생성된다" - {
        val titleInput = "title"
        val contentInput = "content"

        val post = Post(titleInput, contentInput, placeInput, userInput)

        post.title shouldBe titleInput
        post.content shouldBe contentInput
        post.place shouldBe placeInput
        post.user shouldBe userInput
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
})
