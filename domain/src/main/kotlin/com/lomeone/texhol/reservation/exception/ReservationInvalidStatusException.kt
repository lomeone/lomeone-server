package com.lomeone.texhol.reservation.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Reservation status is invalid"

class ReservationInvalidStatusException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = ReservationErrorCode.RESERVATION_INVALID_STATUS,
    detail = ExceptionDetail(detail)
)
