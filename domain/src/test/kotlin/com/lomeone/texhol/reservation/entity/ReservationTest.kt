package com.lomeone.texhol.reservation.entity

import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.store.entity.Store
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe

class ReservationTest : FreeSpec({
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
        description = null
    )

    fun createGameSession(store: Store, gameType: GameType) = GameSession.create(
        store = store,
        gameType = gameType,
        session = 1
    )

    fun createPlayer(nickname: String) = Player(nickname = nickname)

    "예약 생성할 때" - {
        "게임 세션과 플레이어로 예약을 생성할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = createGameSession(store, gameType)
            val player = createPlayer("홍길동")

            val reservation = Reservation(
                gameSession = gameSession,
                player = player,
                time = "19:00"
            )

            reservation.gameSession shouldBe gameSession
            reservation.player shouldBe player
            reservation.time shouldBe "19:00"
            reservation.status shouldBe ReservationStatus.WAITING
        }

        "기본 상태는 WAITING이다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = createGameSession(store, gameType)
            val player = createPlayer("홍길동")

            val reservation = Reservation(gameSession, player, "19:00")

            reservation.status shouldBe ReservationStatus.WAITING
        }

        "다양한 시간으로 예약을 생성할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = createGameSession(store, gameType)

            val reservation1 = Reservation(gameSession, createPlayer("홍길동"), "19:00")
            val reservation2 = Reservation(gameSession, createPlayer("김철수"), "19:30")
            val reservation3 = Reservation(gameSession, createPlayer("이영희"), "20:00")

            reservation1.time shouldBe "19:00"
            reservation2.time shouldBe "19:30"
            reservation3.time shouldBe "20:00"
        }
    }

    "예약 상태 변경할 때" - {
        "예약을 등록 상태로 변경할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = createGameSession(store, gameType)
            val player = createPlayer("홍길동")
            val reservation = Reservation(gameSession, player, "19:00")

            reservation.register()

            reservation.status shouldBe ReservationStatus.REGISTERED
        }

        "예약을 취소할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = createGameSession(store, gameType)
            val player = createPlayer("홍길동")
            val reservation = Reservation(gameSession, player, "19:00")

            reservation.cancel()

            reservation.status shouldBe ReservationStatus.CANCELLED
        }

        "등록된 예약을 취소할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = createGameSession(store, gameType)
            val player = createPlayer("홍길동")
            val reservation = Reservation(gameSession, player, "19:00")

            reservation.register()
            reservation.cancel()

            reservation.status shouldBe ReservationStatus.CANCELLED
        }
    }

    "예약 플레이어 변경할 때" - {
        "예약의 플레이어를 변경할 수 있다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = createGameSession(store, gameType)
            val player1 = createPlayer("홍길동")
            val player2 = createPlayer("김철수")
            val reservation = Reservation(gameSession, player1, "19:00")

            reservation.changePlayer(player2)

            reservation.player shouldBe player2
        }

        "플레이어를 변경해도 다른 정보는 유지된다" {
            val store = createStore()
            val gameType = createGameType(store)
            val gameSession = createGameSession(store, gameType)
            val player1 = createPlayer("홍길동")
            val player2 = createPlayer("김철수")
            val reservation = Reservation(gameSession, player1, "19:00")
            reservation.register()

            reservation.changePlayer(player2)

            reservation.time shouldBe "19:00"
            reservation.status shouldBe ReservationStatus.REGISTERED
        }
    }
})
