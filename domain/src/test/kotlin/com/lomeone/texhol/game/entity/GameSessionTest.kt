package com.lomeone.texhol.game.entity

import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class GameSessionTest : FreeSpec({
    fun createStore() = Store(
        name = "강남점",
        location = "서울 강남구",
        address = null,
        imageUrl = "https://example.com/store.jpg"
    )

    fun createGameType(store: Store) = GameType(
        store = store,
        name = "데일리",
        scheduleType = ScheduleType.DAILY,
        dayOfWeek = null,
        description = "매일 진행"
    )

    "게임 세션 생성할 때" - {
        "Store와 GameType으로 게임 세션을 생성할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)

            val gameSession = GameSession.create(
                store = store,
                gameType = gameType,
                session = 1
            )

            gameSession.store shouldBe store
            gameSession.gameType shouldBe gameType
            gameSession.session shouldBe 1
            gameSession.status shouldBe GameSessionStatus.RECRUITING
        }

        "다양한 회차로 게임 세션을 생성할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)

            val session1 = GameSession.create(store, gameType, 1)
            val session2 = GameSession.create(store, gameType, 2)
            val session3 = GameSession.create(store, gameType, 3)

            session1.session shouldBe 1
            session2.session shouldBe 2
            session3.session shouldBe 3
        }

        "기본 상태는 RECRUITING이다" {
            val store = createStore()
            val gameType = createGameType(store)

            val gameSession = GameSession.create(store, gameType, 1)

            gameSession.status shouldBe GameSessionStatus.RECRUITING
        }
    }

    "게임 세션 검증할 때" - {
        "GameType이 다른 Store에 속하면 생성할 수 없다" {
            val store1 = Store(
                name = "강남점",
                location = "서울 강남구",
                address = null,
                imageUrl = "https://example.com/store.jpg"
            )
            val store2 = Store(
                name = "홍대점",
                location = "서울 마포구",
                address = null,
                imageUrl = "https://example.com/store.jpg"
            )
            val gameType = createGameType(store1)

            shouldThrow<IllegalArgumentException> {
                GameSession.create(
                    store = store2, // 다른 Store
                    gameType = gameType, // store1의 GameType
                    session = 1
                )
            }
        }
    }

    "게임 세션 상태 변경할 때" - {
        "게임을 마감할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = GameSession.create(store, gameType, 1)

            gameSession.close()

            gameSession.status shouldBe GameSessionStatus.CLOSED
        }

        "게임을 조기 마감할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = GameSession.create(store, gameType, 1)

            gameSession.earlyClose()

            gameSession.status shouldBe GameSessionStatus.EARLY_CLOSE
        }

        "마감된 게임을 다시 열 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = GameSession.create(store, gameType, 1)

            gameSession.close()
            gameSession.reOpen()

            gameSession.status shouldBe GameSessionStatus.RECRUITING
        }

        "조기 마감된 게임을 다시 열 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = GameSession.create(store, gameType, 1)

            gameSession.earlyClose()
            gameSession.reOpen()

            gameSession.status shouldBe GameSessionStatus.RECRUITING
        }
    }
})
