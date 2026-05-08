package com.lomeone.texhol.reservation.exception

import com.lomeone.eunoia.exception.ErrorCode
import com.lomeone.eunoia.exception.ExceptionCategory

object ReservationErrorCode {
    const val ERROR_CODE_PREFIX = "reservation"
    val RESERVATION_NOT_FOUND = ErrorCode(
        code = "${ERROR_CODE_PREFIX}/reservation-not-found",
        exceptionCategory = ExceptionCategory.NOT_FOUND
    )
}
