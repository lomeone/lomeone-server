package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterReservation(
    private val reservationRepository: ReservationRepository,
    private val gameEntryRepository: GameEntryRepository
) {
    @Transactional
    operator fun invoke(command: RegisterReservationCommand): RegisterReservationResult {
        val reservation = reservationRepository.findByIdOrNull(command.reservationId)
            ?: throw ReservationNotFoundException(detail = mapOf("reservationId" to command.reservationId))

        reservation.register()

        val gameEntry = GameEntry.create(
            gameSession = reservation.gameSession,
            player = reservation.player,
            initialPayment = command.paymentMethod
        )
        val savedGameEntry = gameEntryRepository.save(gameEntry)

        return RegisterReservationResult(gameEntryId = savedGameEntry.id)
    }
}

data class RegisterReservationCommand(
    val reservationId: Long,
    val paymentMethod: PaymentMethod
)

data class RegisterReservationResult(
    val gameEntryId: Long
)
