package com.lomeone.texhol.store.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class StoreTest : FreeSpec({
    "매장 생성할 때" - {
        "필수 정보로 매장을 생성할 수 있다" {
            val name = "강남점"
            val location = "서울 강남구"
            val address = "서울 강남구 테헤란로 123"
            val imageUrl = "https://example.com/store.jpg"

            val store = Store(
                name = name,
                location = location,
                address = address,
                imageUrl = imageUrl
            )

            store.name shouldBe name
            store.location shouldBe location
            store.address shouldBe address
            store.imageUrl shouldBe imageUrl
        }

        "주소 없이도 매장을 생성할 수 있다" {
            val store = Store(
                name = "강남점",
                location = "서울 강남구",
                address = null,
                imageUrl = "https://example.com/store.jpg"
            )

            store.address shouldBe null
            store.name shouldNotBe null
            store.location shouldNotBe null
        }
    }

    "매장 정보 수정할 때" - {
        "매장 이름을 수정할 수 있다" {
            val store = Store(
                name = "강남점",
                location = "서울 강남구",
                address = null,
                imageUrl = "https://example.com/store.jpg"
            )

            store.updateInfo(name = "강남본점")

            store.name shouldBe "강남본점"
        }

        "매장 위치를 수정할 수 있다" {
            val store = Store(
                name = "강남점",
                location = "서울 강남구",
                address = null,
                imageUrl = "https://example.com/store.jpg"
            )

            store.updateInfo(location = "서울 서초구")

            store.location shouldBe "서울 서초구"
        }

        "여러 정보를 동시에 수정할 수 있다" {
            val store = Store(
                name = "강남점",
                location = "서울 강남구",
                address = null,
                imageUrl = "https://example.com/store.jpg"
            )

            store.updateInfo(
                name = "강남본점",
                location = "서울 서초구",
                address = "서울 서초구 강남대로 456",
                imageUrl = "https://example.com/updated.jpg"
            )

            store.name shouldBe "강남본점"
            store.location shouldBe "서울 서초구"
            store.address shouldBe "서울 서초구 강남대로 456"
            store.imageUrl shouldBe "https://example.com/updated.jpg"
        }

        "일부 정보만 선택적으로 수정할 수 있다" {
            val store = Store(
                name = "강남점",
                location = "서울 강남구",
                address = "서울 강남구 테헤란로 123",
                imageUrl = "https://example.com/store.jpg"
            )

            store.updateInfo(name = "강남본점")

            store.name shouldBe "강남본점"
            store.location shouldBe "서울 강남구" // 변경 안됨
            store.address shouldBe "서울 강남구 테헤란로 123" // 변경 안됨
        }
    }
})
