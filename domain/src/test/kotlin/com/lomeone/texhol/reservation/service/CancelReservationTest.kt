package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import org.springframework.data.repository.findByIdOrNull
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.entity.ReservationStatus
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class CancelReservationTest : BehaviorSpec({
    val reservationRepository = mockk<ReservationRepository>()
    val cancelReservation = CancelReservation(reservationRepository)

    Given("예약이 존재할 때") {
        val reservationId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val player = Player(nickname = "홍길동")
        val reservation = Reservation(gameSession = gameSession, player = player, time = "19:00")

        every { reservationRepository.findByIdOrNull(reservationId) } returns reservation

        When("예약을 취소하면") {
            val command = CancelReservationCommand(reservationId = reservationId)
            cancelReservation(command)

            Then("예약 상태가 CANCELLED로 변경된다") {
                reservation.status shouldBe ReservationStatus.CANCELLED
            }
        }
    }

    Given("예약이 존재하지 않을 때") {
        val reservationId = 999L
        every { reservationRepository.findByIdOrNull(reservationId) } returns null

        When("예약을 취소하려고 하면") {
            val command = CancelReservationCommand(reservationId = reservationId)

            Then("ReservationNotFoundException이 발생한다") {
                shouldThrow<ReservationNotFoundException> {
                    cancelReservation(command)
                }
            }
        }
    }
})
