package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameEntryNotAliveException
import com.lomeone.texhol.game.exception.GameEntryNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class AddReBuyTest : BehaviorSpec({
    val gameEntryRepository = mockk<GameEntryRepository>()
    val addReBuy = AddReBuy(gameEntryRepository)

    Given("게임 엔트리가 ALIVE 상태일 때") {
        val gameEntryId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val player = Player(nickname = "홍길동")
        val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)

        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns gameEntry

        When("리바이를 추가하면") {
            val command = AddReBuyCommand(
                gameEntryId = gameEntryId,
                paymentMethod = PaymentMethod.CARD
            )
            addReBuy(command)

            Then("리바이가 추가된다") {
                gameEntry.buyInRecords.size shouldBe 2
                gameEntry.reBuyCount shouldBe 1
            }
        }
    }

    Given("게임 엔트리가 SIT_OUT 상태일 때") {
        val gameEntryId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val player = Player(nickname = "홍길동")
        val gameEntry = GameEntry.create(gameSession, player, PaymentMethod.CASH)
        gameEntry.sitOut()

        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns gameEntry

        When("리바이를 추가하려고 하면") {
            val command = AddReBuyCommand(
                gameEntryId = gameEntryId,
                paymentMethod = PaymentMethod.CASH
            )

            Then("GameEntryNotAliveException이 발생한다") {
                shouldThrow<GameEntryNotAliveException> {
                    addReBuy(command)
                }
            }
        }
    }

    Given("게임 엔트리가 존재하지 않을 때") {
        val gameEntryId = 999L
        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns null

        When("리바이를 추가하려고 하면") {
            val command = AddReBuyCommand(
                gameEntryId = gameEntryId,
                paymentMethod = PaymentMethod.CASH
            )

            Then("GameEntryNotFoundException이 발생한다") {
                shouldThrow<GameEntryNotFoundException> {
                    addReBuy(command)
                }
            }
        }
    }
})
