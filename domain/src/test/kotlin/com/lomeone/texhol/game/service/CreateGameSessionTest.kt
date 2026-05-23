package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.Game
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.game.exception.GameNotFoundException
import com.lomeone.texhol.game.exception.GameSessionAlreadyExistException
import com.lomeone.texhol.game.exception.GameStoreMismatchException
import com.lomeone.texhol.game.repository.GameRepository
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class CreateGameSessionTest : BehaviorSpec({
    val storeRepository = mockk<StoreRepository>()
    val gameRepository = mockk<GameRepository>()
    val gameSessionRepository = mockk<GameSessionRepository>()
    val createGameSession = CreateGameSession(storeRepository, gameRepository, gameSessionRepository)

    Given("매장과 게임이 존재하고 중복 세션이 없을 때") {
        val storeId = 1L
        val gameId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)

        every { storeRepository.findByIdOrNull(storeId) } returns store
        every { gameRepository.findByIdOrNull(gameId) } returns game
        every { gameSessionRepository.existsByStoreAndGameAndSession(store, game, 1) } returns false
        every { gameSessionRepository.save(any()) } answers { firstArg() }

        When("게임 세션을 생성하면") {
            val command = CreateGameSessionCommand(storeId = storeId, gameId = gameId, session = 1)
            createGameSession(command)

            Then("게임 세션이 저장된다") {
                verify {
                    gameSessionRepository.save(match {
                        it.store == store && it.game == game && it.session == 1
                    })
                }
            }
        }
    }

    Given("동일한 게임 세션이 이미 존재할 때") {
        val storeId = 1L
        val gameId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)

        every { storeRepository.findByIdOrNull(storeId) } returns store
        every { gameRepository.findByIdOrNull(gameId) } returns game
        every { gameSessionRepository.existsByStoreAndGameAndSession(store, game, 1) } returns true

        When("게임 세션을 생성하려고 하면") {
            val command = CreateGameSessionCommand(storeId = storeId, gameId = gameId, session = 1)

            Then("GameSessionAlreadyExistException이 발생한다") {
                shouldThrow<GameSessionAlreadyExistException> {
                    createGameSession(command)
                }
            }
        }
    }

    Given("게임이 요청 매장에 속하지 않을 때") {
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val otherStore = Store(name = "홍대점", location = "서울 마포구", address = null, imageUrl = "")
        val game = Game(store = otherStore, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)

        every { storeRepository.findByIdOrNull(1L) } returns store
        every { gameRepository.findByIdOrNull(2L) } returns game

        When("게임 세션을 생성하려고 하면") {
            val command = CreateGameSessionCommand(storeId = 1L, gameId = 2L, session = 1)

            Then("GameStoreMismatchException이 발생한다") {
                shouldThrow<GameStoreMismatchException> {
                    createGameSession(command)
                }
            }
        }
    }

    Given("매장이 존재하지 않을 때") {
        every { storeRepository.findByIdOrNull(999L) } returns null

        When("게임 세션을 생성하려고 하면") {
            val command = CreateGameSessionCommand(storeId = 999L, gameId = 1L, session = 1)

            Then("StoreNotFoundException이 발생한다") {
                shouldThrow<StoreNotFoundException> {
                    createGameSession(command)
                }
            }
        }
    }

    Given("게임이 존재하지 않을 때") {
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        every { storeRepository.findByIdOrNull(1L) } returns store
        every { gameRepository.findByIdOrNull(999L) } returns null

        When("게임 세션을 생성하려고 하면") {
            val command = CreateGameSessionCommand(storeId = 1L, gameId = 999L, session = 1)

            Then("GameNotFoundException이 발생한다") {
                shouldThrow<GameNotFoundException> {
                    createGameSession(command)
                }
            }
        }
    }
})
