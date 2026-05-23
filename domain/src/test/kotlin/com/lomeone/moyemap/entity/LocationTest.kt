package com.lomeone.moyemap.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class LocationTest : FreeSpec({
    "Location 생성" - {
        "모든 필드를 제공하여 위치를 생성할 수 있다" {
            val location = Location(
                name = "홍대입구역 3번 출구 라운지",
                address = "서울특별시 마포구 양화로 160 (동교동)",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남"
            )

            location.name shouldBe "홍대입구역 3번 출구 라운지"
            location.address shouldBe "서울특별시 마포구 양화로 160 (동교동)"
            location.latitude shouldBe 37.5571
            location.longitude shouldBe 126.9243
            location.region shouldBe "홍대/연남"
        }
    }

    "Location 수정" - {
        "위치명을 수정할 수 있다" {
            val location = Location(
                name = "홍대입구역 3번 출구 라운지",
                address = "서울특별시 마포구 양화로 160",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남"
            )

            location.update(name = "홍대입구역 4번 출구 라운지")

            location.name shouldBe "홍대입구역 4번 출구 라운지"
        }

        "주소를 수정할 수 있다" {
            val location = Location(
                name = "홍대입구역 3번 출구 라운지",
                address = "서울특별시 마포구 양화로 160",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남"
            )

            location.update(address = "서울특별시 마포구 양화로 161")

            location.address shouldBe "서울특별시 마포구 양화로 161"
        }

        "좌표를 수정할 수 있다" {
            val location = Location(
                name = "홍대입구역 3번 출구 라운지",
                address = "서울특별시 마포구 양화로 160",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남"
            )

            location.update(latitude = 37.5572, longitude = 126.9244)

            location.latitude shouldBe 37.5572
            location.longitude shouldBe 126.9244
        }

        "여러 필드를 동시에 수정할 수 있다" {
            val location = Location(
                name = "홍대입구역 3번 출구 라운지",
                address = "서울특별시 마포구 양화로 160",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남"
            )

            location.update(
                name = "강남역 11번 출구 라운지바",
                address = "서울특별시 강남구 테헤란로 123",
                latitude = 37.4979,
                longitude = 127.0276,
                region = "강남"
            )

            location.name shouldBe "강남역 11번 출구 라운지바"
            location.address shouldBe "서울특별시 강남구 테헤란로 123"
            location.latitude shouldBe 37.4979
            location.longitude shouldBe 127.0276
            location.region shouldBe "강남"
        }

        "일부 필드만 선택적으로 수정할 수 있다" {
            val location = Location(
                name = "홍대입구역 3번 출구 라운지",
                address = "서울특별시 마포구 양화로 160",
                latitude = 37.5571,
                longitude = 126.9243,
                region = "홍대/연남"
            )

            location.update(name = "홍대입구역 4번 출구")

            location.name shouldBe "홍대입구역 4번 출구"
            location.address shouldBe "서울특별시 마포구 양화로 160" // 변경 안됨
            location.latitude shouldBe 37.5571 // 변경 안됨
        }
    }
})
