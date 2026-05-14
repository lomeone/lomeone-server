package com.lomeone.texhol.reservation.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Reservation not found"

class ReservationNotFoundException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = ReservationErrorCode.RESERVATION_NOT_FOUND,
    detail = ExceptionDetail(detail)
)
