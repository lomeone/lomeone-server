package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.Game
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.game.exception.GameSessionNotFoundException
import com.lomeone.texhol.game.repository.GameSessionRepository
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.exception.PlayerNotFoundException
import com.lomeone.texhol.player.repository.PlayerRepository
import com.lomeone.texhol.reservation.exception.ReservationAlreadyExistException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class CreateReservationTest : BehaviorSpec({
    val reservationRepository = mockk<ReservationRepository>()
    val gameSessionRepository = mockk<GameSessionRepository>()
    val playerRepository = mockk<PlayerRepository>()
    val createReservation = CreateReservation(reservationRepository, gameSessionRepository, playerRepository)

    Given("게임 세션과 플레이어가 존재하고 중복 예약이 없을 때") {
        val gameSessionId = 1L
        val playerId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)
        val player = Player(id = playerId, nickname = "홍길동")

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { playerRepository.findByIdOrNull(playerId) } returns player
        every { reservationRepository.existsByGameSessionAndPlayer(gameSession, player) } returns false
        every { reservationRepository.save(any()) } answers { firstArg() }

        When("예약을 생성하면") {
            val command = CreateReservationCommand(
                gameSessionId = gameSessionId,
                playerId = playerId,
                reservationTime = "19:00"
            )
            createReservation(command)

            Then("예약이 저장된다") {
                verify {
                    reservationRepository.save(match {
                        it.gameSession == gameSession && it.player == player && it.time == "19:00"
                    })
                }
            }
        }
    }

    Given("동일한 게임 세션에 이미 예약한 플레이어일 때") {
        val gameSessionId = 1L
        val playerId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)
        val player = Player(id = playerId, nickname = "홍길동")

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { playerRepository.findByIdOrNull(playerId) } returns player
        every { reservationRepository.existsByGameSessionAndPlayer(gameSession, player) } returns true

        When("예약을 생성하려고 하면") {
            val command = CreateReservationCommand(
                gameSessionId = gameSessionId,
                playerId = playerId,
                reservationTime = "19:00"
            )

            Then("ReservationAlreadyExistException이 발생한다") {
                shouldThrow<ReservationAlreadyExistException> {
                    createReservation(command)
                }
            }
        }
    }

    Given("게임 세션이 존재하지 않을 때") {
        val gameSessionId = 999L
        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns null

        When("예약을 생성하려고 하면") {
            val command = CreateReservationCommand(
                gameSessionId = gameSessionId,
                playerId = 1L,
                reservationTime = "19:00"
            )

            Then("GameSessionNotFoundException이 발생한다") {
                shouldThrow<GameSessionNotFoundException> {
                    createReservation(command)
                }
            }
        }
    }

    Given("플레이어가 존재하지 않을 때") {
        val gameSessionId = 1L
        val playerId = 999L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val game = Game(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, game = game, session = 1)

        every { gameSessionRepository.findByIdOrNull(gameSessionId) } returns gameSession
        every { playerRepository.findByIdOrNull(playerId) } returns null

        When("예약을 생성하려고 하면") {
            val command = CreateReservationCommand(
                gameSessionId = gameSessionId,
                playerId = playerId,
                reservationTime = "19:00"
            )

            Then("PlayerNotFoundException이 발생한다") {
                shouldThrow<PlayerNotFoundException> {
                    createReservation(command)
                }
            }
        }
    }
})
