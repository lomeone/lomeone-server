package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.GameSessionStatus
import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class UpdateGameSessionStatusTest : BehaviorSpec({
    val gameSessionRepository = mockk<GameSessionRepository>()
    val updateGameSessionStatus = UpdateGameSessionStatus(gameSessionRepository)

    Given("게임 세션이 존재할 때") {
        val gameSessionId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession

        When("상태를 CLOSED로 변경하면") {
            val command = UpdateGameSessionStatusCommand(
                gameSessionId = gameSessionId,
                status = GameSessionStatus.CLOSED
            )
            updateGameSessionStatus(command)

            Then("게임 세션이 종료된다") {
                gameSession.status shouldBe GameSessionStatus.CLOSED
            }
        }
    }

    Given("게임 세션이 존재하지 않을 때") {
        every { gameSessionRepository.findByIdOrNull(999L) } returns null

        When("상태를 변경하려고 하면") {
            val command = UpdateGameSessionStatusCommand(
                gameSessionId = 999L,
                status = GameSessionStatus.CLOSED
            )

            Then("GameSessionNotFoundException이 발생한다") {
                shouldThrow<GameSessionNotFoundException> {
                    updateGameSessionStatus(command)
                }
            }
        }
    }
})
