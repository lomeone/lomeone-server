package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.game.entity.Game
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class GetPaymentStatsByGameTest : BehaviorSpec({
    val gameSessionRepository = mockk<GameSessionRepository>()
    val gameEntryRepository = mockk<GameEntryRepository>()
    val getPaymentStatsByGame = GetPaymentStatsByGame(gameSessionRepository, gameEntryRepository)

    Given("게임이 존재하고 다양한 결제 수단의 게임 엔트리가 있을 때") {
        val gameSessionId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)
        val player1 = Player(nickname = "홍길동")
        val player2 = Player(nickname = "김철수")
        val player3 = Player(nickname = "이영희")

        val gameEntry1 = GameEntry.create(gameSession, player1, PaymentMethod.CASH)
        gameEntry1.addReBuy(PaymentMethod.CASH)

        val gameEntry2 = GameEntry.create(gameSession, player2, PaymentMethod.CARD)
        gameEntry2.addReBuy(PaymentMethod.CARD)
        gameEntry2.addReBuy(PaymentMethod.CARD)

        val gameEntry3 = GameEntry.create(gameSession, player3, PaymentMethod.POINTS)

        val gameEntries = listOf(gameEntry1, gameEntry2, gameEntry3)

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { gameEntryRepository.findByGameSession(gameSession) } returns gameEntries

        When("해당 게임의 결제 통계를 조회하면") {
            val command = GetPaymentStatsByGameCommand(gameSessionId = gameSessionId)
            val result = getPaymentStatsByGame(command)

            Then("각 결제 수단별 개수와 총합이 반환된다") {
                result.cash shouldBe 2  // player1: 초기 1 + 리바이 1
                result.card shouldBe 3  // player2: 초기 1 + 리바이 2
                result.points shouldBe 1  // player3: 초기 1
                result.total shouldBe 6
            }
        }
    }

    Given("게임이 존재하지만 게임 엔트리가 없을 때") {
        val gameSessionId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { gameEntryRepository.findByGameSession(gameSession) } returns emptyList()

        When("해당 게임의 결제 통계를 조회하면") {
            val command = GetPaymentStatsByGameCommand(gameSessionId = gameSessionId)
            val result = getPaymentStatsByGame(command)

            Then("모든 결제 통계가 0이다") {
                result.cash shouldBe 0
                result.card shouldBe 0
                result.points shouldBe 0
                result.total shouldBe 0
            }
        }
    }

    Given("게임이 존재하고 모든 결제가 현금일 때") {
        val gameSessionId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)
        val player1 = Player(nickname = "홍길동")
        val player2 = Player(nickname = "김철수")

        val gameEntry1 = GameEntry.create(gameSession, player1, PaymentMethod.CASH)
        val gameEntry2 = GameEntry.create(gameSession, player2, PaymentMethod.CASH)
        gameEntry2.addReBuy(PaymentMethod.CASH)

        val gameEntries = listOf(gameEntry1, gameEntry2)

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { gameEntryRepository.findByGameSession(gameSession) } returns gameEntries

        When("해당 게임의 결제 통계를 조회하면") {
            val command = GetPaymentStatsByGameCommand(gameSessionId = gameSessionId)
            val result = getPaymentStatsByGame(command)

            Then("현금만 집계된다") {
                result.cash shouldBe 3
                result.card shouldBe 0
                result.points shouldBe 0
                result.total shouldBe 3
            }
        }
    }

    Given("게임이 존재하지 않을 때") {
        val gameSessionId = 999L
        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns null

        When("해당 게임의 결제 통계를 조회하면") {
            val command = GetPaymentStatsByGameCommand(gameSessionId = gameSessionId)

            Then("GameSessionNotFoundException이 발생한다") {
                shouldThrow<GameSessionNotFoundException> {
                    getPaymentStatsByGame(command)
                }
            }
        }
    }
})
