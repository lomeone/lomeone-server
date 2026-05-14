package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.player.service.FindOrCreatePlayer
import com.lomeone.texhol.player.service.FindOrCreatePlayerCommand
import com.lomeone.texhol.reservation.exception.ReservationAlreadyExistException
import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.repository.findByIdOrNull

class ChangeReservationPlayerTest : BehaviorSpec({
    val reservationRepository = mockk<ReservationRepository>()
    val findOrCreatePlayer = mockk<FindOrCreatePlayer>()
    val changeReservationPlayer = ChangeReservationPlayer(reservationRepository, findOrCreatePlayer)

    Given("예약이 존재하고 새로운 닉네임이 주어졌을 때") {
        val reservationId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val oldPlayer = Player(nickname = "홍길동")
        val newPlayer = Player(nickname = "김철수")
        val reservation = Reservation(gameSession = gameSession, player = oldPlayer, time = "19:00")

        every { reservationRepository.findByIdOrNull(reservationId) } returns reservation
        every { findOrCreatePlayer(FindOrCreatePlayerCommand("김철수")) } returns newPlayer
        every { reservationRepository.existsByGameSessionAndPlayer(gameSession, newPlayer) } returns false

        When("플레이어를 변경하면") {
            val command = ChangeReservationPlayerCommand(
                reservationId = reservationId,
                newNickname = "김철수"
            )
            changeReservationPlayer(command)

            Then("예약의 플레이어가 변경된다") {
                reservation.player shouldBe newPlayer
            }
        }
    }

    Given("예약이 존재하지 않을 때") {
        val reservationId = 999L
        every { reservationRepository.findByIdOrNull(reservationId) } returns null

        When("플레이어를 변경하려고 하면") {
            val command = ChangeReservationPlayerCommand(
                reservationId = reservationId,
                newNickname = "김철수"
            )

            Then("ReservationNotFoundException이 발생한다") {
                shouldThrow<ReservationNotFoundException> {
                    changeReservationPlayer(command)
                }
            }
        }
    }

    Given("변경할 플레이어가 이미 같은 게임에 예약 중일 때") {
        val reservationId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val oldPlayer = Player(nickname = "홍길동")
        val newPlayer = Player(nickname = "김철수")
        val reservation = Reservation(gameSession = gameSession, player = oldPlayer, time = "19:00")

        every { reservationRepository.findByIdOrNull(reservationId) } returns reservation
        every { findOrCreatePlayer(FindOrCreatePlayerCommand("김철수")) } returns newPlayer
        every { reservationRepository.existsByGameSessionAndPlayer(gameSession, newPlayer) } returns true

        When("플레이어를 변경하려고 하면") {
            val command = ChangeReservationPlayerCommand(
                reservationId = reservationId,
                newNickname = "김철수"
            )

            Then("ReservationAlreadyExistException이 발생한다") {
                shouldThrow<ReservationAlreadyExistException> {
                    changeReservationPlayer(command)
                }
            }
        }
    }
})
