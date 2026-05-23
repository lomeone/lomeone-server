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

class GetGameEntriesByGameTest : BehaviorSpec({
    val gameSessionRepository = mockk<GameSessionRepository>()
    val gameEntryRepository = mockk<GameEntryRepository>()
    val getGameEntriesByGame = GetGameEntriesByGame(gameSessionRepository, gameEntryRepository)

    Given("게임이 존재하고 여러 게임 엔트리가 있을 때") {
        val gameSessionId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)
        val player1 = Player(nickname = "홍길동")
        val player2 = Player(nickname = "김철수")
        val player3 = Player(nickname = "이영희")
        val gameEntries = listOf(
            GameEntry.create(gameSession, player1, PaymentMethod.CASH),
            GameEntry.create(gameSession, player2, PaymentMethod.CARD),
            GameEntry.create(gameSession, player3, PaymentMethod.POINTS)
        )

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { gameEntryRepository.findByGameSession(gameSession) } returns gameEntries

        When("해당 게임의 게임 엔트리 목록을 조회하면") {
            val command = GetGameEntriesByGameCommand(gameSessionId = gameSessionId)
            val result = getGameEntriesByGame(command)

            Then("모든 게임 엔트리가 반환된다") {
                result.size shouldBe 3
                result[0].player.nickname shouldBe "홍길동"
                result[1].player.nickname shouldBe "김철수"
                result[2].player.nickname shouldBe "이영희"
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

        When("해당 게임의 게임 엔트리 목록을 조회하면") {
            val command = GetGameEntriesByGameCommand(gameSessionId = gameSessionId)
            val result = getGameEntriesByGame(command)

            Then("빈 목록이 반환된다") {
                result.size shouldBe 0
            }
        }
    }

    Given("게임이 존재하지 않을 때") {
        val gameSessionId = 999L
        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns null

        When("해당 게임의 게임 엔트리 목록을 조회하면") {
            val command = GetGameEntriesByGameCommand(gameSessionId = gameSessionId)

            Then("GameSessionNotFoundException이 발생한다") {
                shouldThrow<GameSessionNotFoundException> {
                    getGameEntriesByGame(command)
                }
            }
        }
    }
})
