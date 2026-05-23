package com.lomeone.texhol.game.entity

import com.lomeone.texhol.game.entity.Game
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe

class GameEntryTest : FreeSpec({
    fun createStore() = Store(
        name = "강남점",
        location = "서울 강남구",
        address = null,
        imageUrl = "https://example.com/store.jpg"
    )

    fun createGame(store: Store) = Game(
        store = store,
        name = "데일리",
        scheduleType = ScheduleType.DAILY,
        dayOfWeek = null,
        description = null
    )

    fun createGameSession(store: Store, game: Game) = GameSession.create(
        store = store,
        game = game,
        session = 1
    )

    fun createPlayer(nickname: String) = Player(nickname = nickname)

    "게임 참가 생성할 때" - {
        "초기 결제 정보와 함께 게임 참가를 생성할 수 있다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player = createPlayer("홍길동")

            val gameEntry = GameEntry.create(
                gameSession = gameSession,
                player = player,
                initialPayment = PaymentMethod.CASH
            )

            gameEntry.gameSession shouldBe gameSession
            gameEntry.player shouldBe player
            gameEntry.status shouldBe GameEntryStatus.ALIVE
            gameEntry.buyInRecords shouldHaveSize 1
            gameEntry.buyInRecords[0].type shouldBe BuyInType.INITIAL
            gameEntry.buyInRecords[0].paymentMethod shouldBe PaymentMethod.CASH
        }

        "기본 상태는 ALIVE다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player = createPlayer("홍길동")

            val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CARD)

            gameEntry.status shouldBe GameEntryStatus.ALIVE
        }

        "다양한 결제 수단으로 참가할 수 있다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)

            val entry1 = GameEntry.create(gameSession, createPlayer("홍길동"), PaymentMethod.CASH)
            val entry2 = GameEntry.create(gameSession, createPlayer("김철수"), PaymentMethod.CARD)
            val entry3 = GameEntry.create(gameSession, createPlayer("이영희"), PaymentMethod.POINTS)

            entry1.buyInRecords[0].paymentMethod shouldBe PaymentMethod.CASH
            entry2.buyInRecords[0].paymentMethod shouldBe PaymentMethod.CARD
            entry3.buyInRecords[0].paymentMethod shouldBe PaymentMethod.POINTS
        }
    }

    "바이인 기록 관리할 때" - {
        "리바이를 추가할 수 있다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player = createPlayer("홍길동")
            val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)

            gameEntry.addReBuy(PaymentMethod.CARD)

            gameEntry.buyInRecords shouldHaveSize 2
            gameEntry.buyInRecords[0].type shouldBe BuyInType.INITIAL
            gameEntry.buyInRecords[1].type shouldBe BuyInType.RE_BUY
            gameEntry.buyInRecords[1].paymentMethod shouldBe PaymentMethod.CARD
        }

        "여러 번 리바이할 수 있다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player = createPlayer("홍길동")
            val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)

            gameEntry.addReBuy(PaymentMethod.CARD)
            gameEntry.addReBuy(PaymentMethod.POINTS)
            gameEntry.addReBuy(PaymentMethod.CASH)

            gameEntry.buyInRecords shouldHaveSize 4 // INITIAL 1개 + RE_BUY 3개
            gameEntry.reBuyCount shouldBe 3
        }

        "리바이 횟수를 계산할 수 있다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player = createPlayer("홍길동")
            val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)

            gameEntry.reBuyCount shouldBe 0

            gameEntry.addReBuy(PaymentMethod.CARD)
            gameEntry.reBuyCount shouldBe 1

            gameEntry.addReBuy(PaymentMethod.CASH)
            gameEntry.reBuyCount shouldBe 2
        }

        "SIT_OUT 상태에서는 리바이할 수 없다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player = createPlayer("홍길동")
            val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)

            gameEntry.sitOut()

            shouldThrow<Exception> {
                gameEntry.addReBuy(PaymentMethod.CARD)
            }
        }
    }

    "게임 참가 상태 변경할 때" - {
        "참가자를 SIT_OUT 상태로 변경할 수 있다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player = createPlayer("홍길동")
            val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)

            gameEntry.sitOut()

            gameEntry.status shouldBe GameEntryStatus.SIT_OUT
        }

        "SIT_OUT 상태에서 다시 게임으로 복귀할 수 있다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player = createPlayer("홍길동")
            val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)

            gameEntry.sitOut()
            gameEntry.returnToGame()

            gameEntry.status shouldBe GameEntryStatus.ALIVE
        }
    }

    "플레이어 변경할 때" - {
        "참가자의 플레이어를 변경할 수 있다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player1 = createPlayer("홍길동")
            val player2 = createPlayer("김철수")
            val gameEntry = GameEntry.create(gameSession, player1, PaymentMethod.CASH)

            gameEntry.changePlayer(player2)

            gameEntry.player shouldBe player2
        }

        "플레이어를 변경해도 바이인 기록은 유지된다" {
            val store = createStore()
            val game = createGame(store)
            val gameSession = createGameSession(store, game)
            val player1 = createPlayer("홍길동")
            val player2 = createPlayer("김철수")
            val gameEntry = GameEntry.create(gameSession, player1, PaymentMethod.CASH)
            gameEntry.addReBuy(PaymentMethod.CARD)

            gameEntry.changePlayer(player2)

            gameEntry.buyInRecords shouldHaveSize 2
            gameEntry.reBuyCount shouldBe 1
        }
    }
})
