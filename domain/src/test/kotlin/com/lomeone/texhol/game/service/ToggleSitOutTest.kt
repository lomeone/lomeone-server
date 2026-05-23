package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.entity.GameEntryStatus
import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameEntryNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
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

class ToggleSitOutTest : BehaviorSpec({
    val gameEntryRepository = mockk<GameEntryRepository>()
    val toggleSitOut = ToggleSitOut(gameEntryRepository)

    Given("게임 엔트리가 ALIVE 상태일 때") {
        val gameEntryId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)
        val player = Player(nickname = "홍길동")
        val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)

        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns gameEntry

        When("SIT OUT을 토글하면") {
            val command = ToggleSitOutCommand(gameEntryId = gameEntryId)
            toggleSitOut(command)

            Then("상태가 SIT_OUT으로 변경된다") {
                gameEntry.status shouldBe GameEntryStatus.SIT_OUT
            }
        }
    }

    Given("게임 엔트리가 SIT_OUT 상태일 때") {
        val gameEntryId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)
        val player = Player(nickname = "홍길동")
        val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)
        gameEntry.sitOut()

        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns gameEntry

        When("SIT OUT을 토글하면") {
            val command = ToggleSitOutCommand(gameEntryId = gameEntryId)
            toggleSitOut(command)

            Then("상태가 ALIVE로 변경된다") {
                gameEntry.status shouldBe GameEntryStatus.ALIVE
            }
        }
    }

    Given("게임 엔트리가 존재하지 않을 때") {
        val gameEntryId = 999L
        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns null

        When("SIT OUT을 토글하려고 하면") {
            val command = ToggleSitOutCommand(gameEntryId = gameEntryId)

            Then("GameEntryNotFoundException이 발생한다") {
                shouldThrow<GameEntryNotFoundException> {
                    toggleSitOut(command)
                }
            }
        }
    }
})
