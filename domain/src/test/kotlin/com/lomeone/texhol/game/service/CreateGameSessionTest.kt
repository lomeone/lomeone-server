package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.game.exception.GameSessionAlreadyExistException
import com.lomeone.texhol.game.exception.GameTypeNotFoundException
import com.lomeone.texhol.game.exception.GameTypeStoreMismatchException
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.game.repository.GameTypeRepository
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
    val gameTypeRepository = mockk<GameTypeRepository>()
    val gameSessionRepository = mockk<GameSessionRepository>()
    val createGameSession = CreateGameSession(storeRepository, gameTypeRepository, gameSessionRepository)

    Given("매장과 게임 타입이 존재하고 중복 세션이 없을 때") {
        val storeId = 1L
        val gameTypeId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)

        every { storeRepository.findByIdOrNull(storeId) } returns store
        every { gameTypeRepository.findByIdOrNull(gameTypeId) } returns gameType
        every { gameSessionRepository.existsByStoreAndGameTypeAndSession(store, gameType, 1) } returns false
        every { gameSessionRepository.save(any()) } answers { firstArg() }

        When("게임 세션을 생성하면") {
            val command = CreateGameSessionCommand(storeId = storeId, gameTypeId = gameTypeId, session = 1)
            createGameSession(command)

            Then("게임 세션이 저장된다") {
                verify {
                    gameSessionRepository.save(match {
                        it.store == store && it.gameType == gameType && it.session == 1
                    })
                }
            }
        }
    }

    Given("동일한 게임 세션이 이미 존재할 때") {
        val storeId = 1L
        val gameTypeId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)

        every { storeRepository.findByIdOrNull(storeId) } returns store
        every { gameTypeRepository.findByIdOrNull(gameTypeId) } returns gameType
        every { gameSessionRepository.existsByStoreAndGameTypeAndSession(store, gameType, 1) } returns true

        When("게임 세션을 생성하려고 하면") {
            val command = CreateGameSessionCommand(storeId = storeId, gameTypeId = gameTypeId, session = 1)

            Then("GameSessionAlreadyExistException이 발생한다") {
                shouldThrow<GameSessionAlreadyExistException> {
                    createGameSession(command)
                }
            }
        }
    }

    Given("게임 타입이 요청 매장에 속하지 않을 때") {
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val otherStore = Store(name = "홍대점", location = "서울 마포구", address = null, imageUrl = "")
        val gameType = GameType(store = otherStore, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)

        every { storeRepository.findByIdOrNull(1L) } returns store
        every { gameTypeRepository.findByIdOrNull(2L) } returns gameType

        When("게임 세션을 생성하려고 하면") {
            val command = CreateGameSessionCommand(storeId = 1L, gameTypeId = 2L, session = 1)

            Then("GameTypeStoreMismatchException이 발생한다") {
                shouldThrow<GameTypeStoreMismatchException> {
                    createGameSession(command)
                }
            }
        }
    }

    Given("매장이 존재하지 않을 때") {
        every { storeRepository.findByIdOrNull(999L) } returns null

        When("게임 세션을 생성하려고 하면") {
            val command = CreateGameSessionCommand(storeId = 999L, gameTypeId = 1L, session = 1)

            Then("StoreNotFoundException이 발생한다") {
                shouldThrow<StoreNotFoundException> {
                    createGameSession(command)
                }
            }
        }
    }

    Given("게임 타입이 존재하지 않을 때") {
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        every { storeRepository.findByIdOrNull(1L) } returns store
        every { gameTypeRepository.findByIdOrNull(999L) } returns null

        When("게임 세션을 생성하려고 하면") {
            val command = CreateGameSessionCommand(storeId = 1L, gameTypeId = 999L, session = 1)

            Then("GameTypeNotFoundException이 발생한다") {
                shouldThrow<GameTypeNotFoundException> {
                    createGameSession(command)
                }
            }
        }
    }
})
