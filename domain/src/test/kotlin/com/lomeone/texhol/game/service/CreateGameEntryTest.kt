package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameEntryAlreadyExistException
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class CreateGameEntryTest : BehaviorSpec({
    val gameSessionRepository = mockk<GameSessionRepository>()
    val playerRepository = mockk<PlayerRepository>()
    val gameEntryRepository = mockk<GameEntryRepository>()
    val createGameEntry = CreateGameEntry(gameSessionRepository, playerRepository, gameEntryRepository)

    Given("게임과 플레이어가 모두 존재할 때") {
        val gameSessionId = 1L
        val playerId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val player = Player(nickname = "홍길동")

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { playerRepository.findByIdOrNull(playerId) } returns player
        every { gameEntryRepository.existsByGameSessionAndPlayer_Id(gameSession, player.id) } returns false
        every { gameEntryRepository.save(any()) } answers { firstArg() }

        When("게임 엔트리를 생성하면") {
            val command = CreateGameEntryCommand(
                gameSessionId = gameSessionId,
                playerId = playerId,
                paymentMethod = PaymentMethod.CASH
            )
            createGameEntry(command)

            Then("게임 엔트리가 생성된다") {
                verify {
                    gameEntryRepository.save(match {
                        it.player == player && it.buyInRecords.size == 1
                    })
                }
            }
        }
    }

    Given("게임이 존재하지 않을 때") {
        val gameSessionId = 999L
        val playerId = 1L
        val player = Player(nickname = "홍길동")

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns null
        every { playerRepository.findByIdOrNull(playerId) } returns player

        When("게임 엔트리를 생성하려고 하면") {
            val command = CreateGameEntryCommand(
                gameSessionId = gameSessionId,
                playerId = playerId,
                paymentMethod = PaymentMethod.CASH
            )

            Then("GameSessionNotFoundException이 발생한다") {
                shouldThrow<GameSessionNotFoundException> {
                    createGameEntry(command)
                }
            }
        }
    }

    Given("플레이어가 존재하지 않을 때") {
        val gameSessionId = 1L
        val playerId = 999L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { playerRepository.findByIdOrNull(playerId) } returns null

        When("게임 엔트리를 생성하려고 하면") {
            val command = CreateGameEntryCommand(
                gameSessionId = gameSessionId,
                playerId = playerId,
                paymentMethod = PaymentMethod.CASH
            )

            Then("PlayerNotFoundException이 발생한다") {
                shouldThrow<PlayerNotFoundException> {
                    createGameEntry(command)
                }
            }
        }
    }

    Given("동일한 게임에 이미 참가한 플레이어일 때") {
        val gameSessionId = 1L
        val playerId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val player = Player(id = playerId, nickname = "홍길동")

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { playerRepository.findByIdOrNull(playerId) } returns player
        every { gameEntryRepository.existsByGameSessionAndPlayer_Id(gameSession, player.id) } returns true

        When("게임 엔트리를 생성하려고 하면") {
            val command = CreateGameEntryCommand(
                gameSessionId = gameSessionId,
                playerId = playerId,
                paymentMethod = PaymentMethod.CASH
            )

            Then("GameEntryAlreadyExistException이 발생한다") {
                shouldThrow<GameEntryAlreadyExistException> {
                    createGameEntry(command)
                }
            }
        }
    }
})
