package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameEntryAlreadyExistException
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.reservation.entity.Reservation
import com.lomeone.texhol.reservation.entity.ReservationStatus
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import com.lomeone.texhol.game.entity.GameSession
import com.lomeone.texhol.game.entity.GameType
import com.lomeone.texhol.game.entity.ScheduleType
import com.lomeone.texhol.player.entity.Player
import com.lomeone.texhol.store.entity.Store
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class RegisterReservationTest : BehaviorSpec({
    val reservationRepository = mockk<ReservationRepository>()
    val gameEntryRepository = mockk<GameEntryRepository>()
    val registerReservation = RegisterReservation(reservationRepository, gameEntryRepository)

    Given("예약이 존재할 때") {
        val reservationId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val player = Player(nickname = "홍길동")
        val reservation = Reservation(gameSession = gameSession, player = player, time = "19:00")

        every { reservationRepository.findByIdOrNull(reservationId) } returns reservation
        every { gameEntryRepository.existsByGameSessionAndPlayer_Id(gameSession, player.id) } returns false
        every { gameEntryRepository.save(any()) } answers { firstArg() }

        When("예약을 게임에 등록하면") {
            val command = RegisterReservationCommand(
                reservationId = reservationId,
                paymentMethod = PaymentMethod.CASH
            )
            registerReservation(command)

            Then("예약 상태가 REGISTERED로 변경되고 게임 엔트리가 생성된다") {
                reservation.status shouldBe ReservationStatus.REGISTERED
                verify {
                    gameEntryRepository.save(match {
                        it.player == player && it.buyInRecords.size == 1
                    })
                }
            }
        }
    }

    Given("예약이 존재하지 않을 때") {
        val reservationId = 999L
        every { reservationRepository.findByIdOrNull(reservationId) } returns null

        When("예약을 게임에 등록하려고 하면") {
            val command = RegisterReservationCommand(
                reservationId = reservationId,
                paymentMethod = PaymentMethod.CASH
            )

            Then("ReservationNotFoundException이 발생한다") {
                shouldThrow<ReservationNotFoundException> {
                    registerReservation(command)
                }
            }
        }
    }

    Given("예약자의 게임 엔트리가 이미 존재할 때") {
        val reservationId = 1L
        val store = Store(name = "강남점", location = "서울 강남구", address = null, imageUrl = "")
        val gameType = GameType(store = store, name = "NLH", scheduleType = ScheduleType.DAILY, dayOfWeek = null, description = null)
        val gameSession = GameSession.create(store = store, gameType = gameType, session = 1)
        val player = Player(id = 1L, nickname = "홍길동")
        val reservation = Reservation(gameSession = gameSession, player = player, time = "19:00")

        every { reservationRepository.findByIdOrNull(reservationId) } returns reservation
        every { gameEntryRepository.existsByGameSessionAndPlayer_Id(gameSession, player.id) } returns true

        When("예약을 게임에 등록하려고 하면") {
            val command = RegisterReservationCommand(
                reservationId = reservationId,
                paymentMethod = PaymentMethod.CASH
            )

            Then("GameEntryAlreadyExistException이 발생한다") {
                shouldThrow<GameEntryAlreadyExistException> {
                    registerReservation(command)
                }
            }
        }
    }
})
