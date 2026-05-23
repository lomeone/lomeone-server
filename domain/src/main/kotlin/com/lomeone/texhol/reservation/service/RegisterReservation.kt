package com.lomeone.texhol.reservation.service

import com.lomeone.texhol.game.entity.GameEntry
import com.lomeone.texhol.game.entity.PaymentMethod
import com.lomeone.texhol.game.exception.GameEntryAlreadyExistException
import com.lomeone.texhol.game.repository.GameEntryRepository
import com.lomeone.texhol.reservation.exception.ReservationNotFoundException
import com.lomeone.texhol.reservation.repository.ReservationRepository
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import com.lomeone.texhol.common.TexholTransactional

@Service
class RegisterReservation(
    private val reservationRepository: ReservationRepository,
    private val gameEntryRepository: GameEntryRepository
) {
    private val logger = KotlinLogging.logger {}

    @TexholTransactional
    operator fun invoke(command: RegisterReservationCommand): RegisterReservationResult {
        logger.info { "Registering reservation: reservationId=${command.reservationId}, paymentMethod=${command.paymentMethod}" }
        val reservation = reservationRepository.findByIdOrNull(command.reservationId)
            ?: throw ReservationNotFoundException(detail = mapOf("reservationId" to command.reservationId))

        if (gameEntryRepository.existsByGameSessionAndPlayer_Id(reservation.gameSession, reservation.player.id)) {
            throw GameEntryAlreadyExistException(
                detail = mapOf("reservationId" to command.reservationId, "playerId" to reservation.player.id)
            )
        }

        reservation.register()

        val gameEntry = GameEntry.create(
            gameSession = reservation.gameSession,
            player = reservation.player,
            initialPayment = command.paymentMethod
        )
        val savedGameEntry = gameEntryRepository.save(gameEntry)
        logger.info { "Reservation registered: reservationId=${command.reservationId}, gameEntryId=${savedGameEntry.id}" }

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
