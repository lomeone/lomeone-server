package com.lomeone.texhol.player.entity

import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class PlayerTest : FreeSpec({
    "플레이어 생성할 때" - {
        "닉네임으로 플레이어를 생성할 수 있다" {
            val nickname = "홍길동"

            val player = Player(nickname = nickname)

            player.nickname shouldBe nickname
        }

        "다양한 닉네임으로 플레이어를 생성할 수 있다" {
            val player1 = Player(nickname = "홍길동")
            val player2 = Player(nickname = "김철수")
            val player3 = Player(nickname = "이영희")

            player1.nickname shouldBe "홍길동"
            player2.nickname shouldBe "김철수"
            player3.nickname shouldBe "이영희"
        }
    }
})
