package com.lomeone.texhol.game.service

import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.game.exception.GameNameAlreadyExistException
import com.lomeone.texhol.game.repository.GameRepository
import com.lomeone.texhol.store.entity.Store
import com.lomeone.texhol.store.exception.StoreNotFoundException
import com.lomeone.texhol.store.repository.StoreRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class CreateGameTest : BehaviorSpec({
    val storeRepository = mockk<StoreRepository>()
    val gameRepository = mockk<GameRepository>()
    val createGame = CreateGame(storeRepository, gameRepository)

    Given("매장이 존재하고 같은 이름의 게임이 없을 때") {
        val storeId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")

        every { storeRepository.findByIdOrNull(storeId) } returns store
        every { gameRepository.existsByStoreAndName(store, "NLH") } returns false
        every { gameRepository.save(any()) } answers { firstArg() }

        When("게임을 생성하면") {
            val command = CreateGameCommand(
                storeId = storeId,
                name = "NLH",
                scheduleType = ScheduleType.WEEKLY,
                dayOfWeek = 5,
                description = "금요일 게임"
            )
            createGame(command)

            Then("게임이 저장된다") {
                verify {
                    gameRepository.save(match {
                        it.store == store && it.name == "NLH" && it.dayOfWeek == 5
                    })
                }
            }
        }
    }

    Given("같은 매장에 같은 이름의 게임이 이미 있을 때") {
        val storeId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")

        every { storeRepository.findByIdOrNull(storeId) } returns store
        every { gameRepository.existsByStoreAndName(store, "NLH") } returns true

        When("게임을 생성하려고 하면") {
            val command = CreateGameCommand(
                storeId = storeId,
                name = "NLH",
                scheduleType = ScheduleType.DAILY,
                dayOfWeek = null,
                description = null
            )

            Then("GameNameAlreadyExistException이 발생한다") {
                shouldThrow<GameNameAlreadyExistException> {
                    createGame(command)
                }
            }
        }
    }

    Given("매장이 존재하지 않을 때") {
        every { storeRepository.findByIdOrNull(999L) } returns null

        When("게임을 생성하려고 하면") {
            val command = CreateGameCommand(
                storeId = 999L,
                name = "NLH",
                scheduleType = ScheduleType.DAILY,
                dayOfWeek = null,
                description = null
            )

            Then("StoreNotFoundException이 발생한다") {
                shouldThrow<StoreNotFoundException> {
                    createGame(command)
                }
            }
        }
    }
})
