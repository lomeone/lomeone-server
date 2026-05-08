package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameEntryAlreadyExistException
import com.lomeone.texhol.game.exception.GameEntryNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.service.FindOrCreatePlayer
import com.lomeone.texhol.player.service.FindOrCreatePlayerCommand
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class ChangeGameEntryPlayerTest : BehaviorSpec({
    val gameEntryRepository = mockk<GameEntryRepository>()
    val findOrCreatePlayer = mockk<FindOrCreatePlayer>()
    val changeGameEntryPlayer = ChangeGameEntryPlayer(gameEntryRepository, findOrCreatePlayer)

    Given("게임 엔트리가 존재하고 새로운 닉네임이 주어졌을 때") {
        val gameEntryId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val oldPlayer = Player(nickname = "홍길동")
        val newPlayer = Player(nickname = "김철수")
        val gameEntry = GameEntry.create(gameSession, oldPlayer, PaymentMethod.CASH)

        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns gameEntry
        every { findOrCreatePlayer(FindOrCreatePlayerCommand("김철수")) } returns newPlayer
        every { gameEntryRepository.existsByGameSessionAndPlayer_Id(gameSession, newPlayer.id) } returns false

        When("플레이어를 변경하면") {
            val command = ChangeGameEntryPlayerCommand(
                gameEntryId = gameEntryId,
                newNickname = "김철수"
            )
            changeGameEntryPlayer(command)

            Then("게임 엔트리의 플레이어가 변경된다") {
                gameEntry.player shouldBe newPlayer
            }
        }
    }

    Given("게임 엔트리가 존재하지 않을 때") {
        val gameEntryId = 999L
        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns null

        When("플레이어를 변경하려고 하면") {
            val command = ChangeGameEntryPlayerCommand(
                gameEntryId = gameEntryId,
                newNickname = "김철수"
            )

            Then("GameEntryNotFoundException이 발생한다") {
                shouldThrow<GameEntryNotFoundException> {
                    changeGameEntryPlayer(command)
                }
            }
        }
    }

    Given("변경할 플레이어가 이미 같은 게임에 참가 중일 때") {
        val gameEntryId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val oldPlayer = Player(nickname = "홍길동")
        val newPlayer = Player(id = 2L, nickname = "김철수")
        val gameEntry = GameEntry.create(gameSession, oldPlayer, PaymentMethod.CASH)

        every { gameEntryRepository.findByIdOrNull(gameEntryId) } returns gameEntry
        every { findOrCreatePlayer(FindOrCreatePlayerCommand("김철수")) } returns newPlayer
        every { gameEntryRepository.existsByGameSessionAndPlayer_Id(gameSession, newPlayer.id) } returns true

        When("플레이어를 변경하려고 하면") {
            val command = ChangeGameEntryPlayerCommand(
                gameEntryId = gameEntryId,
                newNickname = "김철수"
            )

            Then("GameEntryAlreadyExistException이 발생한다") {
                shouldThrow<GameEntryAlreadyExistException> {
                    changeGameEntryPlayer(command)
                }
            }
        }
    }
})
