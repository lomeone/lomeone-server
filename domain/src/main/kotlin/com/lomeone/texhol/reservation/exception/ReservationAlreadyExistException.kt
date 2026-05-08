package com.lomeone.texhol.reservation.exception

import com.lomeone.eunoia.exception.EunioaException
import com.lomeone.eunoia.exception.ExceptionDetail

private const val MESSAGE = "Reservation already exists"

class ReservationAlreadyExistException(
    message: String = MESSAGE,
    detail: Map<String, Any> = mapOf()
) : EunioaException(
    message = message,
    errorCode = ReservationErrorCode.RESERVATION_ALREADY_EXIST,
    detail = ExceptionDetail(detail)
)
